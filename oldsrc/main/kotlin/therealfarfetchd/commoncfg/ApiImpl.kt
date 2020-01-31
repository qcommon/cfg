package therealfarfetchd.commoncfg

import org.apache.logging.log4j.LogManager
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.cmds.impl.CommandDispatcherImpl
import therealfarfetchd.commoncfg.cmds.impl.CommandRegistryImpl
import therealfarfetchd.commoncfg.cmds.impl.CvarRegistryImpl
import therealfarfetchd.commoncfg.cmds.impl.LogOutput
import therealfarfetchd.commoncfg.cmds.impl.MultiOutput
import therealfarfetchd.commoncfg.cmds.impl.PersistableRegistryImpl
import therealfarfetchd.commoncfg.common.TerminalManager
import therealfarfetchd.commoncfg.common.term.impl.StandardTerminal

class ApiImpl : CommonCfgApi.Mutable {

  val conLog = LogManager.getLogger("Console")

  val term = StandardTerminal()
  val tman = TerminalManager(term)

  override val commandRegistry = CommandRegistryImpl()
  override val persistRegistry = PersistableRegistryImpl(commandRegistry)
  override val termOutput = MultiOutput(LogOutput(conLog), tman)
  override val cvarRegistry = CvarRegistryImpl(commandRegistry, persistRegistry)
  override val dispatcher = CommandDispatcherImpl(commandRegistry, termOutput)

}