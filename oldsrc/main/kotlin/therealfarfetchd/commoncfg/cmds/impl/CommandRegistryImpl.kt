package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.Command
import therealfarfetchd.commoncfg.api.cmds.CommandRegistry
import therealfarfetchd.commoncfg.api.cmds.ExecContext

class CommandRegistryImpl : CommandRegistry.Mutable {

  override val commands: Collection<Command>
    get() = commandMap.values

  private var commandMap: Map<String, Command> = emptyMap()

  private var aliasMap: Map<String, String> = emptyMap()

  init {
    registerDefaultCommands(this)

    registerSimple("alias") { _, args ->
      aliasMap += args[0] to args[1]
    }
  }

  override fun lookup(name: String): Command? =
    aliasMap[name]?.let { alias -> AliasWrapper(name, alias) }
    ?: commandMap[name]

  override fun register(c: Command) {
    val name = c.name
    if (name in commandMap) {
      error("Tried to register duplicate command $name")
    }
    commandMap += name to c
  }

  override fun registerSimple(name: String, op: (ctx: ExecContext, args: List<String>) -> Unit) = register(object : Command {
    override val name: String = name
    override val desc: String? = null
    override fun exec(ctx: ExecContext, vararg args: String) = op(ctx, args.toList())
  })

}