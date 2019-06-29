package therealfarfetchd.commoncfg.common.rconclient

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.provide
import therealfarfetchd.commoncfg.common.rconclient.ResponseType.AuthResult
import therealfarfetchd.commoncfg.common.rconclient.ResponseType.CmdOutput
import therealfarfetchd.commoncfg.common.rconclient.ResponseType.Unknown
import java.io.IOException
import java.net.Socket
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

class RconClient : CommandInitializer {

  // rcon c->s packet format
  // i32 -> following data length
  // i32 -> message id
  // i32 -> message type (2: exec command, 3: auth req)
  // str -> message content
  // 0x00

  // rcon s->c packet format
  // i32 -> following data length
  // i32 -> message id / -1 for auth fail
  // i32 -> message type (0: command/error response, 2: auth)
  // str -> message content
  // 0x00
  // 0x00

  var rconAddr = ""
  var rconPort = 25575
  var rconPassword = ""

  // required because Socket only saves the connected IP, not the domain
  private var connectedRconAddr = ""
  private var sock: Socket? = null

  private var commsThread: Thread? = null

  private val queue = ConcurrentLinkedQueue<String>()

  private val out = CommonCfgApi.instance.termOutput

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    api.cvarRegistry.provide("rcon_address", this::rconAddr)
    api.cvarRegistry.provide("rcon_port", this::rconPort)
    api.cvarRegistry.provide("rcon_password", this::rconPassword)
    api.commandRegistry.registerSimple("rcon") { _, args ->
      ensureInitialized()
      queue.add(args.joinToString(" "))
    }
    api.commandRegistry.registerSimple("rcon_reset") { _, _ -> reset() }
  }

  private fun initThread(sock: Socket) = thread(name = "RCON Client", isDaemon = true) {
    var packetIndex = 0
    var lastPacketTime = 0L

    var lastRecvPacketId = 0
    var lastRecvPacketType = Unknown
    var lastRecvPacketStr = ""
    var lastRecvPacketTime = 0L
    var lastRecvPacketHandled = false

    var isAuthenticated = false

    while (this.sock === sock && isInitialized()) {
      // recv
      if (sock.getInputStream().available() > 0) {
        val istr = sock.getInputStream()
        val arr = ByteArray(4110)
        val len = try {
          istr.read(arr)
        } catch (e: IOException) {
          reset()
          break
        }

        val r = readPacket(Unpooled.wrappedBuffer(arr, 0, len))
        if (r.responseTo == lastRecvPacketId) lastRecvPacketStr += r.text
        else lastRecvPacketStr = r.text
        lastRecvPacketId = r.responseTo
        lastRecvPacketType = r.type
        lastRecvPacketTime = System.currentTimeMillis()
        lastRecvPacketHandled = false
      }
      if (!lastRecvPacketHandled && (System.currentTimeMillis() - lastRecvPacketTime > 100 || lastRecvPacketType == AuthResult)) {
        when (lastRecvPacketType) {
          CmdOutput -> out.println(lastRecvPacketStr)
          AuthResult -> {
            isAuthenticated = lastRecvPacketId != -1
            if (!isAuthenticated) {
              out.println("Permission denied")
              queue.clear()
            }
          }
          else -> Unit
        }
        lastRecvPacketHandled = true
      }
      // send
      if (queue.isNotEmpty() && System.currentTimeMillis() - lastPacketTime > 100) {
        val p = if (!isAuthenticated) {
          mkpacket(packetIndex, 3, rconPassword)
        } else {
          mkpacket(packetIndex, 2, queue.remove())
        }
        packetIndex += 1
        try {
          val ostr = sock.getOutputStream()
          ostr.write(p.array(), p.arrayOffset(), p.readableBytes())
        } catch (e: IOException) {
          reset()
          break
        }
        lastPacketTime = System.currentTimeMillis()
      }
      Thread.sleep(25)
    }
  }

  private fun mkpacket(index: Int, type: Int, str: String): ByteBuf {
    val buf = Unpooled.buffer()
    val strBytes = str.toByteArray()
    buf.writeIntLE(9 + strBytes.size)
    buf.writeIntLE(index)
    buf.writeIntLE(type)
    buf.writeBytes(strBytes)
    buf.writeByte(0)
    return buf
  }

  private fun readPacket(buf: ByteBuf): Response {
    val strLen = buf.readIntLE() - 10
    val responseTo = buf.readIntLE()
    val type = buf.readIntLE()
    val str = String(ByteArray(strLen).also { buf.readBytes(it) })
    val t = when (type) {
      0 -> CmdOutput
      2 -> AuthResult
      else -> Unknown
    }
    return Response(t, responseTo, str)
  }

  private fun isInitialized() =
    sock != null &&
    sock?.isClosed != true &&
    sock?.port == rconPort &&
    connectedRconAddr == rconAddr

  private fun ensureInitialized() {
    if (!isInitialized()) initSocket()
  }

  private fun initSocket() {
    reset()
    try {
      queue.clear()
      sock = Socket(rconAddr, rconPort)
      connectedRconAddr = rconAddr
      commsThread = initThread(sock!!)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun reset() {
    try {
      sock?.close()
    } catch (e: IOException) {
    }
    sock = null
  }

}

private data class Response(val type: ResponseType, val responseTo: Int, val text: String)

private enum class ResponseType { CmdOutput, AuthResult, Unknown }