package therealfarfetchd.commoncfg

import net.minecraft.client.MinecraftClient
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.api.cmds.provide

object Configuration {

  var consoleWidth = 80
  var consoleHeight = 25
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
    api.cvarRegistry.provide("cl_showdebug", MinecraftClient.getInstance().options::debugEnabled)
    api.cvarRegistry.provide("cl_showprofiler", MinecraftClient.getInstance().options::debugProfilerEnabled)
    api.cvarRegistry.provide("cl_showtps", MinecraftClient.getInstance().options::debugTpsEnabled)

    CommonCfgClient.rcon.onInitialize(api)
  }

}