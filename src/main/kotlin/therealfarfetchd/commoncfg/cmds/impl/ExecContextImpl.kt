package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.CommandDispatcher
import therealfarfetchd.commoncfg.api.cmds.CommandRegistry
import therealfarfetchd.commoncfg.api.cmds.ExecContext
import therealfarfetchd.commoncfg.api.cmds.ExecSource

class ExecContextImpl(override val source: ExecSource, override val registry: CommandRegistry, override val disp: CommandDispatcher, val output: Output) : ExecContext {

  override fun println(x: Any?) {
    output.println(x.toString())
  }

  override fun printf(fmt: String, vararg args: Any?) {
    println(String.format(fmt, *args))
  }

}