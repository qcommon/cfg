package therealfarfetchd.commoncfg

import org.apache.logging.log4j.LogManager
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.binds.BindsManager
import therealfarfetchd.commoncfg.cmds.impl.*
import therealfarfetchd.commoncfg.common.TerminalManager
import therealfarfetchd.commoncfg.common.term.impl.StandardTerminal

class ApiImpl : CommonCfgApi.Mutable {

  val conLog = LogManager.getLogger("Console")

  val term = StandardTerminal()
  val tman = TerminalManager(term)
  val bm = BindsManager()

  override val commandRegistry = CommandRegistryImpl()
  override val persistRegistry = PersistableRegistryImpl(commandRegistry)
  override val termOutput = MultiOutput(LogOutput(conLog), tman)
  override val cvarRegistry = CvarRegistryImpl(commandRegistry, persistRegistry)
  override val dispatcher = CommandDispatcherImpl(commandRegistry, termOutput)

  init {
    bm.onInitialize(this)
    persistRegistry.register(bm)
  }

}