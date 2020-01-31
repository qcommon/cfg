package therealfarfetchd.commoncfg

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry
import net.fabricmc.fabric.api.event.client.ClientTickCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.CommandInitializer
import therealfarfetchd.commoncfg.client.font.BDFParser
import therealfarfetchd.commoncfg.client.font.FontWrapper
import therealfarfetchd.commoncfg.client.gui.TerminalScreen
import therealfarfetchd.commoncfg.common.rconclient.RconClient

object CommonCfgClient : ClientModInitializer {

  private lateinit var kbConsole: FabricKeyBinding

  private var lastFont: String? = null
  private var font: FontWrapper? = null

  val rcon = RconClient()

  override fun onInitializeClient() {
    kbConsole = FabricKeyBinding.Builder.create(Identifier(ModID, "open_console"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, "key.categories.misc").build()
    KeyBindingRegistry.INSTANCE.register(kbConsole)

    ClientTickCallback.EVENT.register(ClientTickCallback { client ->
      CommonCfg.api.tman.update()
      val currentScreen = client.currentScreen
      if (kbConsole.wasPressed() && currentScreen !is TerminalScreen) {
        client.openScreen(TerminalScreen(currentScreen, CommonCfg.api.term))
      }
    })
  }

  fun onGameLoaded() {
    CommonCfg.setupEntrypoints(FabricLoader.getInstance().getEntrypoints("commands_client", CommandInitializer::class.java))
    CommonCfg.setupEnv()
    CommonCfgApi.instance.persistRegistry.save()
  }

  fun getFont(): FontWrapper? {
    if (lastFont == Configuration.consoleFont) return font
    try {
      val f = BDFParser.read(getFontFile())
      font?.destroy()
      font = FontWrapper(f)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    lastFont = Configuration.consoleFont
    return font
  }

  private fun getFontFile() = getFontFileFor(Configuration.consoleFont)

  private fun getFontFileFor(font: String) = Identifier(ModID, "fonts/$font.bdf")

}