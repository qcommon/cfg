package therealfarfetchd.commoncfg.api.cmds

import java.util.function.BiConsumer

interface CommandRegistry {

  val commands: Collection<Command>

  fun lookup(name: String): Command?

  interface Mutable : CommandRegistry {

    fun register(c: Command)

    fun registerSimple(name: String, op: (ctx: ExecContext, args: List<String>) -> Unit)

    @JvmDefault
    fun registerSimple(name: String, op: BiConsumer<ExecContext, List<String>>) {
      registerSimple(name, op::accept)
    }

  }

}