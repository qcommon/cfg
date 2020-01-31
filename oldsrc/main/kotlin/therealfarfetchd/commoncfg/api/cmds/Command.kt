package therealfarfetchd.commoncfg.api.cmds

interface Command {

  val name: String
  val desc: String?

  fun exec(ctx: ExecContext, vararg args: String)

}