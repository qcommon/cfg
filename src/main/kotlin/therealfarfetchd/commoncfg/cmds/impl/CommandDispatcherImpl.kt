package therealfarfetchd.commoncfg.cmds.impl

import net.fabricmc.loader.api.FabricLoader
import therealfarfetchd.commoncfg.CommonCfg
import therealfarfetchd.commoncfg.api.cmds.CommandDispatcher
import therealfarfetchd.commoncfg.api.cmds.ExecSource
import therealfarfetchd.commoncfg.api.cmds.ExecSource.Script
import therealfarfetchd.commoncfg.api.cmds.Persistable
import java.io.IOException
import java.nio.file.Files

class CommandDispatcherImpl(val reg: CommandRegistryImpl, val output: Output) : CommandDispatcher {

  override fun exec(line: String, source: ExecSource) {
    val ctx = ExecContextImpl(source, reg, this, output)
    val lines = tokenize(line)
    for (c in lines) {
      execLineDo(c, ctx)
    }
  }

  override fun undoExec(line: String, source: ExecSource) {
    val lines = tokenize(line)
    if (lines.size == 1 && lines[0].isNotEmpty() && lines[0][0].startsWith("+")) {
      val ctx = ExecContextImpl(source, reg, this, output)
      val cmd = lines[0].toMutableList()
      cmd[0] = "-${cmd[0].substring(1)}"
      execLineDo(cmd, ctx)
    }
  }

  private fun execLineDo(c: List<String>, ctx: ExecContextImpl) {
    val cmd = c.first()
    val args = c.drop(1)
    val command = reg.lookup(cmd)
    if (command != null) {
      try {
        command.exec(ctx, *args.toTypedArray())
      } catch (e: Exception) {
        output.println("exec error in '$cmd': ${e.message}")
        CommonCfg.logger.error("Command line: ${c.joinToString(" ", transform = Persistable.Companion::escape)}")
        e.printStackTrace()
      }
    } else output.println("Command not found: $cmd")
  }

  override fun execFile(file: String) {
    val file = "$file.cfg"
    val p = FabricLoader.getInstance().configDirectory.toPath().resolve(file)
    try {
      val lines = Files.readAllLines(p)
      for (line in lines) exec(line, Script)
    } catch (e: IOException) {
      output.println("$file not found.")
    }
  }

}

private fun tokenize(s: String): List<List<String>> {
  fun get(i: Int) = if (i in s.indices) s[i] else null

  var esc = false
  var quoted = false

  val commands = mutableListOf<List<String>>()
  var current = mutableListOf<String>()

  val sb = StringBuilder()

  fun nextToken() {
    if (sb.isNotBlank()) current.add(sb.toString())
    sb.clear()
  }

  fun nextCommand() {
    nextToken()
    if (current.isNotEmpty()) commands += current
    current = mutableListOf()
  }

  for ((pos, c) in s.withIndex()) {
    if (esc) {
      sb.append(c)
      esc = false
    } else if (!quoted && c == '/' && get(pos + 1) == '/') {
      break
    } else if (!quoted && c == ';') {
      nextCommand()
    } else if (!quoted && c == ' ') {
      nextToken()
    } else if (c == '"') {
      quoted = !quoted
    } else if (c == '\\') {
      esc = true
    } else {
      sb.append(c)
    }
  }

  nextCommand()

  return commands
}