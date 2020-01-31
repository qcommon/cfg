package therealfarfetchd.commoncfg.api.cmds

interface CommandRegistry {

  val commands: Collection<Command>

  fun lookup(name: String): Command?

  interface Mutable : CommandRegistry {

    fun register(c: Command)

    fun registerSimple(name: String, op: (ctx: ExecContext, args: List<String>) -> Unit)

  }

}