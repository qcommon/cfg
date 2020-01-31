package therealfarfetchd.commoncfg.common.term.impl

import therealfarfetchd.commoncfg.common.term.ColorPalette.Highlight
import therealfarfetchd.commoncfg.common.term.Key
import therealfarfetchd.commoncfg.common.term.Terminal

class StandardTerminal : Terminal {
  private var keyBuffer = emptyList<Key>()
  private var width = 80
  private var height = 25
  private var displayMem = CharArray(0)
  private var attribMem = ByteArray(0)
  private var cursor: Boolean = true
  private var cursorX: Int = 0
  private var cursorY: Int = 0

  // Default values.
  val dbgcol = 0
  val dfgcol = 7
  val dhl = Highlight.Normal

  private var bgcol = dbgcol
  private var fgcol = dfgcol
  private var hl = dhl

  init {
    clear()
  }

  override fun read(): Key? {
    return if (keyBuffer.isNotEmpty()) {
      val c = keyBuffer.first()
      keyBuffer = keyBuffer.drop(1)
      c
    } else {
      null
    }
  }

  override fun bufferKey(c: Key) {
    if (keyBuffer.size < 128) {
      keyBuffer += c
    }
  }

  override fun clear() {
    displayMem = CharArray(width * height) { ' ' }
    attribMem = ByteArray(width * height) { getAttrib() }
  }

  private fun getAttrib(bgcol: Int = this.bgcol, fgcol: Int = this.fgcol, hl: Highlight = this.hl) =
    ((bgcol and 7) or
      ((fgcol and 7) shl 3) or
      (hl.id shl 6)).toByte()

  private fun decode(attrib: Byte) = Triple(
    attrib.toInt() and 0xFF and 7,
    attrib.toInt() and 0xFF shr 3 and 7,
    Highlight.byId(attrib.toInt() and 0xFF shr 6 and 2)
  )

  override fun resetInput() {
    keyBuffer = emptyList()
  }

  override fun resetAttrib() {
    bgcol = dbgcol
    fgcol = dfgcol
    hl = dhl
  }

  override fun cursor() = cursor

  override fun cursor(value: Boolean) {
    cursor = value
  }

  override fun cursorX() = cursorX

  override fun cursorY() = cursorY

  override fun cursorX(x: Int) {
    cursorX = x
  }

  override fun cursorY(y: Int) {
    cursorY = y
  }

  override fun put(x: Int, y: Int, ch: Char) {
    if (x in 0 until width && y in 0 until height) {
      displayMem[x + y * width] = ch
      attribMem[x + y * width] = getAttrib()
    }
  }

  override fun get(x: Int, y: Int): Char? = when {
    x in 0 until width && y in 0 until height -> displayMem[x + y * width]
    else -> null
  }

  private fun getAttrib(x: Int, y: Int): Byte? = when {
    x in 0 until width && y in 0 until height -> attribMem[x + y * width]
    else -> null
  }

  override fun setBGCol(color: Int) = when (color) {
    in 0..7 -> bgcol = color
    9 -> bgcol = dbgcol
    else -> Unit
  }

  override fun getBGCol(x: Int, y: Int) = when {
    x in 0 until width && y in 0 until height -> decode(attribMem[x + y * width]).first
    else -> null
  }

  override fun setFGCol(color: Int) = when (color) {
    in 0..7 -> fgcol = color
    9 -> fgcol = dfgcol
    else -> Unit
  }

  override fun getFGCol(x: Int, y: Int) = when {
    x in 0 until width && y in 0 until height -> decode(attribMem[x + y * width]).second
    else -> null
  }

  override fun setHighlight(highlight: Highlight) {
    hl = highlight
  }

  override fun getHighlight(x: Int, y: Int) = when {
    x in 0 until width && y in 0 until height -> decode(attribMem[x + y * width]).third
    else -> null
  }

  override fun width(): Int = width

  override fun height(): Int = height

  override fun resize(x: Int, y: Int) {
    if (width == x && height == y) return

    val displayMemNew = CharArray(x * y) {
      val dx = it % width
      val dy = it / width
      get(dx, dy) ?: ' '
    }

    val attribMemNew = ByteArray(x * y) {
      val dx = it % width
      val dy = it / width
      getAttrib(dx, dy) ?: getAttrib(dbgcol, dfgcol, dhl)
    }

    displayMem = displayMemNew
    attribMem = attribMemNew
    width = x
    height = y
  }

  override fun scroll(n: Int) {
    if (n < 1) return
    for (x in 0 until width) {
      for (y in 0 until height - n) {
        displayMem[x + y * width] = displayMem[x + (y + n) * width]
      }
    }
    for (x in 0 until width) {
      for (y in height - n until height) {
        displayMem[x + y * width] = ' '
      }
    }
  }

  override fun scrollDown(n: Int) {
    TODO("not implemented")
  }

  override fun scrollLeft(n: Int) {
    TODO("not implemented")
  }

  override fun scrollRight(n: Int) {
    TODO("not implemented")
  }
}