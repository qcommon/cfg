package therealfarfetchd.commoncfg.cmds.impl

import therealfarfetchd.commoncfg.api.cmds.CommandRegistry
import therealfarfetchd.commoncfg.api.cmds.ConVar
import therealfarfetchd.commoncfg.api.cmds.CvarField
import therealfarfetchd.commoncfg.api.cmds.CvarMapper
import therealfarfetchd.commoncfg.api.cmds.CvarRegistry
import therealfarfetchd.commoncfg.api.cmds.PersistableRegistry

class CvarRegistryImpl(private val cr: CommandRegistry.Mutable, private val pr: PersistableRegistry.Mutable) : CvarRegistry.Mutable {

  override val cvars: MutableMap<String, ConVar> = mutableMapOf()

  init {
    cr.registerSimple("reset") { _, args -> cvars[args[0]]?.also { it.value = it.default } }
  }

  override fun <T> provide(name: String, field: CvarField<T>, persistFile: String?, mapper: CvarMapper<T>): ConVar {
    val cvar = SimpleCvar(name, null, field, mapper, persistFile)
    cr.register(cvar)
    pr.register(cvar)
    cvars[name] = cvar
    return cvar
  }

}