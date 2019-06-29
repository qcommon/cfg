package therealfarfetchd.commoncfg.api.cmds

interface ExecContext {

  val source: ExecSource

  val registry: CommandRegistry

  val disp: CommandDispatcher

  fun println(x: Any?)

  fun printf(fmt: String, vararg args: Any?)

}