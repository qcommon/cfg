package therealfarfetchd.commoncfg.common.term.impl

import therealfarfetchd.commoncfg.common.term.ColorPalette
import therealfarfetchd.commoncfg.common.term.Key
import therealfarfetchd.commoncfg.common.term.Terminal

object NullTerminal : Terminal {
  override fun resetAttrib() {}

  override fun setBGCol(color: Int) {}

  override fun getBGCol(x: Int, y: Int): Int? = 0

  override fun setFGCol(color: Int) {}

  override fun getFGCol(x: Int, y: Int): Int? = 1

  override fun setHighlight(highlight: ColorPalette.Highlight) {}

  override fun getHighlight(x: Int, y: Int): ColorPalette.Highlight? = ColorPalette.Highlight.Normal

  override fun clear() {}

  override fun cursor() = true

  override fun cursor(value: Boolean) {}

  override fun cursorX() = 22

  override fun cursorY() = 0

  override fun cursorX(x: Int) {}

  override fun cursorY(y: Int) {}

  override fun read() = null

  override fun bufferKey(c: Key) {}

  override fun resetInput() {}

  override fun put(x: Int, y: Int, ch: Char) {}

  override fun get(x: Int, y: Int) = if (y == 0 && x in 0..21) "Terminal disconnected."[x] else null

  override fun width() = 80

  override fun height() = 25

  override fun resize(x: Int, y: Int) {}

  override fun scroll(n: Int) {}

  override fun scrollDown(n: Int) {}

  override fun scrollLeft(n: Int) {}

  override fun scrollRight(n: Int) {}
}