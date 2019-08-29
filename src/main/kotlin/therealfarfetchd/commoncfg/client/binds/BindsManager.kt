package therealfarfetchd.commoncfg.client.binds

import net.minecraft.client.MinecraftClient
import net.minecraft.client.options.KeyBinding
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandDispatcher
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.ExecSource.Bind
import therealfarfetchd.commoncfg.api.cmds.Persistable
import therealfarfetchd.commoncfg.client.binds.KeyPrecedence.GREATER
import therealfarfetchd.commoncfg.cmds.impl.Output

class BindsManager(val cd: CommandDispatcher) : CommandInitializer, Persistable {

  override val category = "user"

  private val binds = mutableMapOf<KeyCombo, String>()

  private val pressedBinds = mutableSetOf<KeyCombo>()

  fun recalculatePressedKeys(pressed: Set<Key>) {
    var considered = binds.keys.filter { it.keys.all { it in pressed } }
    considered = considered.filter { a -> considered.none { b -> b.getPrecedence(a) == GREATER } }

    val newPressed = considered - pressedBinds
    val released = pressedBinds - considered

    newPressed.mapNotNull(binds::get).forEach { cd.exec(it, Bind) }
    released.mapNotNull(binds::get).forEach { cd.undoExec(it) }

    pressedBinds.removeAll(released)
    pressedBinds.addAll(newPressed)
  }

  override fun write(o: Output) {
    o.println("unbindall")
    binds.entries
      .sortedBy { (k, _) -> k.fmt() }
      .forEach { (k, v) -> o.println("bind ${Persistable.escape(k.fmt())} ${Persistable.escape(v)}") }
  }

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    with(api.commandRegistry) {
      registerSimple("unbindall") { _, _ ->
        binds.clear()
      }

      registerSimple("bind") { ctx, args ->
        val k = KeyCombo.parse(args[0])
        if (k != null) binds[k] = args[1]
        else ctx.println("Invalid key combo: ${args[1]}")
      }

      registerSimple("unbind") { ctx, args ->
        val k = KeyCombo.parse(args[0])
        if (k != null) binds -= k
        else ctx.println("Invalid key combo: ${args[1]}")
      }

      registerSimple("key_listboundkeys") { ctx, _ ->
        binds.forEach { (k, v) -> ctx.println("${Persistable.escape(k.fmt())} : ${Persistable.escape(v)}") }
      }

      registerSimple("key_findbinding") { ctx, args ->
        ctx.println("unimplemented")
      }

      registerSimple("key_listwrapped") { ctx, _ ->
        ctx.println(MinecraftClient.getInstance().options.keysAll.joinToString { it.id })
      }

      registerSimple("+key_wrapper") { _, args ->
        val binding = findBinding(args[0]) ?: return@registerSimple
        binding.incrTimesPressed()
        binding.setPressed(true)
      }

      registerSimple("-key_wrapper") { _, args ->
        val binding = findBinding(args[0]) ?: return@registerSimple
        binding.setPressed(false)
      }

      registerSimple("key_wrapper") { _, args ->
        val binding = findBinding(args[0]) ?: return@registerSimple
        binding.incrTimesPressed()
      }
    }
  }

  fun findBinding(name: String): KeyBinding? {
    return MinecraftClient.getInstance().options.keysAll.singleOrNull { it.id == "key.$name" }
  }

}