package therealfarfetchd.commoncfg.api

import therealfarfetchd.commoncfg.api.cmds.CommandDispatcher
import therealfarfetchd.commoncfg.api.cmds.CommandRegistry
import therealfarfetchd.commoncfg.api.cmds.CvarRegistry
import therealfarfetchd.commoncfg.api.cmds.PersistableRegistry
import therealfarfetchd.commoncfg.cmds.impl.Output

interface CommonCfgApi {

  val termOutput: Output
  val commandRegistry: CommandRegistry
  val dispatcher: CommandDispatcher
  val cvarRegistry: CvarRegistry
  val persistRegistry: PersistableRegistry

  interface Mutable : CommonCfgApi {

    override val commandRegistry: CommandRegistry.Mutable
    override val cvarRegistry: CvarRegistry.Mutable
    override val persistRegistry: PersistableRegistry.Mutable

  }

  companion object {
    lateinit var instance: CommonCfgApi
      @JvmStatic get
      @JvmSynthetic internal set
  }

}