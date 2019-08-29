package therealfarfetchd.commoncfg

import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.provide

object Configuration {

  var consoleWidth
    get() = CommonCfg.api.term.width()
    set(value) {
      CommonCfg.api.term.resize(value, consoleHeight)
    }

  var consoleHeight
    get() = CommonCfg.api.term.height()
    set(value) {
      CommonCfg.api.term.resize(consoleWidth, value)
    }

  var consoleFont = "ter-u16n"

}

class CommandInit : CommandInitializer {

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    api.commandRegistry.registerSimple("test") { ctx, _ ->
      ctx.println("Test!")
    }
  }

}

class CommandInitClient : CommandInitializer {

  override fun onInitialize(api: CommonCfgApi.Mutable) {
    api.cvarRegistry.provide("cl_console_width", Configuration::consoleWidth, "client")
    api.cvarRegistry.provide("cl_console_height", Configuration::consoleHeight, "client")
    api.cvarRegistry.provide("cl_console_font", Configuration::consoleFont, "client")

    CommonCfgClient.rcon.onInitialize(api)
  }

}