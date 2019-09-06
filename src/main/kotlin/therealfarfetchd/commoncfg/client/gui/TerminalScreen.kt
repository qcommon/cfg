package therealfarfetchd.commoncfg.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText
import org.lwjgl.glfw.GLFW
import therealfarfetchd.commoncfg.CommonCfgClient
import therealfarfetchd.commoncfg.ModID
import therealfarfetchd.commoncfg.client.font.FontWrapper
import therealfarfetchd.commoncfg.common.term.ColorPalette
import therealfarfetchd.commoncfg.common.term.KeyModifier.KeyAlt
import therealfarfetchd.commoncfg.common.term.KeyModifier.KeyCtrl
import therealfarfetchd.commoncfg.common.term.KeyModifier.KeyShift
import therealfarfetchd.commoncfg.common.term.Terminal
import java.awt.Color

class TerminalScreen(val parent: Screen?, val terminal: Terminal) : Screen(TranslatableText("gui.$ModID.terminal")) {

  val palette = ColorPalette()

  override fun init() {
    super.init()
    minecraft?.keyboard?.enableRepeatEvents(true)
  }

  override fun render(mouseX: Int, mouseY: Int, delta: Float) {
    parent?.render(mouseX, mouseY, delta)
    super.render(mouseX, mouseY, delta)
    drawTerminal()
  }

  private fun drawTerminal() {
    val mc = minecraft ?: return
    val font = CommonCfgClient.getFont() ?: return
    val scale = mc.window.scaleFactor

    RenderSystem.pushMatrix()
    RenderSystem.scaled(1 / scale, 1 / scale, 1.0)
    RenderSystem.translated((width * scale - getWidgetWidth(font)) / 2, 0.0, 0.0)
    drawBackground(font)

    if (terminal.cursor() && System.currentTimeMillis() / 250 % 2 % 2 == 0L) {
      drawCursorAt(font, terminal.cursorX(), terminal.cursorY())
    }
    for (x in 0 until terminal.width()) for (y in 0 until terminal.height()) {
      font.draw(terminal.get(x, y) ?: ' ', 2 + x * font.charWidth, y * font.charHeight, respectPosition = false,
        color = Color(palette.getColor(terminal.getFGCol(x, y) ?: 0, terminal.getHighlight(x, y) ?: ColorPalette.Highlight.Normal)))
    }

    RenderSystem.popMatrix()
  }

  private fun drawBackground(font: FontWrapper) {
    RenderSystem.disableTexture()
    for (x in 0 until terminal.width()) for (y in 0 until terminal.height()) {
      fill(2 + x * font.charWidth, y * font.charHeight,
        2 + (x + 1) * font.charWidth, (y + 1) * font.charHeight,
        0xCF000000.toInt() or palette.getColor(terminal.getBGCol(x, y) ?: 0))
    }
    val bordercol = 0xFF2160B1.toInt()

    val tw = terminal.width() * font.charWidth
    val th = terminal.height() * font.charHeight

    fill(0, 0, 2, th, bordercol)
    fill(tw, 0, tw + 2, th, bordercol)
    fill(0, th, tw + 2, th + 2, bordercol)
    RenderSystem.enableTexture()
  }

  private fun drawCursorAt(font: FontWrapper, x: Int, y: Int) {
    if (x !in 0 until terminal.width() || y !in 0 until terminal.height()) return
    RenderSystem.disableTexture()
    val x1 = x * font.charWidth
    val y1 = y * font.charHeight
    fill(2 + x1, y1 + font.charHeight - 3, 2 + x1 + font.charWidth, y1 + font.charHeight - 1, 0xFFBBBBBB.toInt())
    RenderSystem.enableTexture()
  }

  override fun keyPressed(key: Int, scancode: Int, modifiers: Int): Boolean {
    if (super.keyPressed(key, scancode, modifiers)) return true

    val result: Char? = when (key) {
      GLFW.GLFW_KEY_BACKSPACE -> '\r'
      GLFW.GLFW_KEY_ENTER -> '\n'
      else -> null
    }

    if (result != null) {
      val mods = listOfNotNull(
        KeyAlt.takeIf { hasAltDown() },
        KeyCtrl.takeIf { hasControlDown() },
        KeyShift.takeIf { hasShiftDown() }
      ).toSet()

      terminal.bufferKey(Pair(result, mods))
    }

    return result != null
  }

  override fun charTyped(c: Char, modifiers: Int): Boolean {
    if (super.charTyped(c, modifiers)) return true
    val mods = listOfNotNull(
      KeyAlt.takeIf { hasAltDown() },
      KeyCtrl.takeIf { hasControlDown() },
      KeyShift.takeIf { hasShiftDown() }
    ).toSet()
    terminal.bufferKey(Pair(c, mods))
    return true
  }

  private fun getWidgetWidth(font: FontWrapper) = terminal.width() * font.charWidth + 4

  private fun getWidgetHeight(font: FontWrapper) = terminal.height() * font.charHeight + 2

  override fun isPauseScreen() = false

  override fun removed() {
    super.removed()
    minecraft?.keyboard?.enableRepeatEvents(false)
  }

}