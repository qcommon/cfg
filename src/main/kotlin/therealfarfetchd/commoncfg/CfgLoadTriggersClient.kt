package therealfarfetchd.commoncfg

import net.minecraft.client.MinecraftClient
import therealfarfetchd.commoncfg.api.CommonCfgApi

object CfgLoadTriggersClient {

  private val cd = CommonCfgApi.instance.dispatcher

  private fun onOpenWorld() {
    cd.execFile("world")
    cd.execFile("player/${MinecraftClient.getInstance().session.username}")
  }

  fun onOpenSPWorld() {
    onOpenWorld()
    cd.execFile("sp")
  }

  fun onOpenMPWorld(ip: String, port: Int) {
    onOpenWorld()
    CommonCfgClient.rcon.rconAddr = ip
    CommonCfgClient.rcon.rconPort = 25575
    if (port == 25565) cd.execFile("server/$ip")
    else cd.execFile("server/$ip:$port")
  }

}