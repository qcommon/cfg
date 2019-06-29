package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.Command
import therealfarfetchd.commoncfg.api.cmds.ExecContext

class AliasWrapper(override val name: String, val exec: String) : Command {

  override val desc: String? = null

  override fun exec(ctx: ExecContext, vararg args: String) {
    ctx.disp.exec(exec, ctx.source)
  }

}