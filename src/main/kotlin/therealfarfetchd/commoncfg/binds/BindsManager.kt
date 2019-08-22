package therealfarfetchd.commoncfg.binds

import net.minecraft.client.MinecraftClient
import net.minecraft.client.options.KeyBinding
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.Persistable
import therealfarfetchd.commoncfg.cmds.impl.Output

class BindsManager : CommandInitializer, Persistable {

  override val category = "user"

  private val binds = mutableMapOf<String, String>()

  override fun write(o: Output) {
    o.println("unbindall")
    binds.forEach { (k, v) -> o.println("bind ${Persistable.escape(k)} ${Persistable.escape(v)}") }
  }

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    with(api.commandRegistry) {
      registerSimple("unbindall") { _, _ ->
        binds.clear()
      }

      registerSimple("bind") { _, args ->
        binds[args[0]] = args[1]
      }

      registerSimple("unbind") { _, args ->
        binds -= args[0]
      }

      registerSimple("key_listboundkeys") { ctx, _ ->
        binds.forEach { (k, v) -> ctx.println("${Persistable.escape(k)} ${Persistable.escape(v)}") }
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
        binding.setPressed(true)
      }
    }
  }

  fun findBinding(name: String): KeyBinding? {
    return MinecraftClient.getInstance().options.keysAll.singleOrNull { it.id == name }
  }

}