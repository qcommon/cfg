package therealfarfetchd.commoncfg.common.term.impl

import therealfarfetchd.commoncfg.common.term.ColorPalette
import therealfarfetchd.commoncfg.common.term.Key
import therealfarfetchd.commoncfg.common.term.Terminal

class RedirectableTerminal : Terminal {
  var termImpl: Terminal = NullTerminal

  override fun read() = termImpl.read()

  override fun bufferKey(c: Key) = termImpl.bufferKey(c)

  override fun resetInput() = termImpl.resetInput()

  override fun resetAttrib() = termImpl.resetAttrib()

  override fun put(x: Int, y: Int, ch: Char) = termImpl.put(x, y, ch)

  override fun get(x: Int, y: Int) = termImpl.get(x, y)

  override fun setBGCol(color: Int) = termImpl.setBGCol(color)

  override fun getBGCol(x: Int, y: Int) = termImpl.getBGCol(x, y)

  override fun setFGCol(color: Int) = termImpl.setFGCol(color)

  override fun getFGCol(x: Int, y: Int) = termImpl.getFGCol(x, y)

  override fun setHighlight(highlight: ColorPalette.Highlight) = termImpl.setHighlight(highlight)

  override fun getHighlight(x: Int, y: Int) = termImpl.getHighlight(x, y)

  override fun width() = termImpl.width()

  override fun height() = termImpl.height()

  override fun resize(x: Int, y: Int) = termImpl.resize(x, y)

  override fun clear() = termImpl.clear()

  override fun cursor() = termImpl.cursor()

  override fun cursor(value: Boolean) = termImpl.cursor(value)

  override fun cursorX() = termImpl.cursorX()

  override fun cursorY() = termImpl.cursorY()

  override fun cursorX(x: Int) = termImpl.cursorX(x)

  override fun cursorY(y: Int) = termImpl.cursorY(y)

  override fun scroll(n: Int) = termImpl.scroll(n)

  override fun scrollDown(n: Int) = termImpl.scrollDown(n)

  override fun scrollLeft(n: Int) = termImpl.scrollLeft(n)

  override fun scrollRight(n: Int) = termImpl.scrollRight(n)
}