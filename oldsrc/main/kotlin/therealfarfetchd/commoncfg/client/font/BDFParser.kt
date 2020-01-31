package therealfarfetchd.commoncfg.client.font

import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.util.*

/**
 * A parser that reads BDF files (only version 1.2 is supported as of now).
 */
object BDFParser {
  fun read(rl: Identifier) =
    read(MinecraftClient.getInstance().resourceManager.getResource(rl).inputStream.bufferedReader().use { it.readLines() })

  fun read(lines: List<String>): BDF {
    val p = ParserImpl()
    lines
      .asSequence()
      .filter { it.isNotBlank() }
      .withIndex()
      .forEach { (i, line) -> p.line(i, line) }
    if (p.pstate != State.End) throw BDFSyntaxException(p.pstate, lines.size, "Expected next token, but got end of file")
    println("BDF, ${p.glyphs.size} glyphs")
    return BDF(glyphs = p.glyphs, defaultGlyph = p.defaultChar)
  }

  private class ParserImpl {
    var pstate = State.Begin
    var bitLine = 0

    var gwidth = 0
    var gheight = 0
    var gxoff = 0
    var gyoff = 0
    var defaultChar: Char = '?'

    lateinit var curGlyph: Glyph
    var glyphs: Map<Char, BDF.IGlyph> = emptyMap()

    fun line(i: Int, str: String) = when (pstate) {
      BDFParser.State.Begin -> handleBegin(str, i)
      BDFParser.State.Header -> handleHeader(str, i)
      BDFParser.State.InterChar -> handleInterChar(str, i)
      BDFParser.State.CharDef -> handleCharDef(str, i)
      BDFParser.State.CharBits -> handleCharBits(str, i)
      BDFParser.State.End -> throw BDFSyntaxException(pstate, i, "There must be nothing after ENDFONT")
    }

    fun handleBegin(str: String, l: Int) {
      val (t, par1) = str.split()
      t.expect(l, "STARTFONT") {
        par1.expect(l, "2.1") {
          pstate = State.Header
        }
      }
    }

    fun handleHeader(str: String, l: Int) {
      val tokens = str.split()
      val t = tokens.first()
      when (t) {
        "FONT", "COMMENT", "COPYRIGHT", "RESOLUTION_X", "AVERAGE_WIDTH",
        "NOTICE", "FOUNDRY", "SIZE", "STARTPROPERTIES", "MIN_SPACE",
        "ENDPROPERTIES", "FAMILY_NAME", "RESOLUTION_Y", "FONT_DESCENT",
        "SLANT", "WEIGHT_NAME", "PIXEL_SIZE", "FONT_ASCENT", "SPACING",
        "POINT_SIZE", "ADD_STYLE_NAME", "SETWIDTH_NAME", "CHARSET_REGISTRY",
        "CHARSET_ENCODING" -> Unit

        "FONTBOUNDINGBOX" -> {
          gwidth = tokens[1].toInt()
          gheight = tokens[2].toInt()
          gxoff = tokens[3].toInt()
          gxoff = tokens[4].toInt()
        }
        "DEFAULT_CHAR" -> defaultChar = tokens[1].toInt().toChar()

        "CHARS" -> pstate = State.InterChar

        else -> unknown(l, t)
      }
    }

    fun handleInterChar(str: String, l: Int) {
      val tokens = str.split()
      val t = tokens.first()
      when (t) {
        "STARTCHAR" -> {
          pstate = State.CharDef
          curGlyph = Glyph(t).apply {
            width = gwidth
            height = gheight
            xOff = gxoff
            yOff = gyoff
          }
        }

        "ENDFONT" -> pstate = State.End
        else -> unknown(l, t)
      }
    }

    fun handleCharDef(str: String, l: Int) {
      val tokens = str.split()
      val t = tokens.first()
      when (t) {
        "SWIDTH" -> Unit // no-op

        "ENCODING" -> curGlyph.codePoint = tokens[1].toInt().toChar()
        "DWIDTH" -> {
          curGlyph.dwidthX = tokens[1].toInt()
          curGlyph.dwidthY = tokens[2].toInt()
        }
        "BBX" -> {
          curGlyph.apply {
            width = tokens[1].toInt()
            height = tokens[2].toInt()
            xOff = tokens[3].toInt()
            yOff = tokens[4].toInt()
          }
        }
        "BITMAP" -> {
          pstate = State.CharBits
          bitLine = 0
        }
      }
    }

    fun handleCharBits(str: String, l: Int) {
      if (str == "ENDCHAR") {
        pstate = State.InterChar
        if (bitLine != curGlyph.height) throw IllegalStateException("Not enough bitmap data! (Got $bitLine, expected ${curGlyph.height}")
        glyphs += curGlyph.codePoint to curGlyph
      } else {
        val wordlen = str.length * 4 - 1
        val data = str.toInt(16)
        for (i in 0 until curGlyph.width) {
          curGlyph.bits[i + bitLine * curGlyph.width] = data ushr wordlen - i and 1 != 0
        }
        bitLine++
      }
    }

    fun String.split() = split(' ')

    fun String.expect(l: Int, v: String, op: () -> Unit) =
      if (this != v) error(l, "Expected $this, got $v!")
      else op()

    fun unknown(l: Int, t: String) = warn(l, "Unknown token in this context: $t, ignoring")

    fun error(l: Int, msg: String): Nothing = throw BDFSyntaxException(pstate, l, msg)

    fun warn(l: Int, msg: String) = Unit //Terminator.Logger.warn("BDF Parser [line $l, state $pstate]: $msg")
  }

  private class Glyph(override val name: String) : BDF.IGlyph {
    override var codePoint: Char = '\u0000'
    override var width: Int = 0
    override var height: Int = 0
    override var xOff: Int = 0
    override var yOff: Int = 0
    override var dwidthX: Int = 0
    override var dwidthY: Int = 0
    override val bits: BitSet = BitSet()
  }

  enum class State {
    Begin, // Start of file. Expect STARTFONT 2.1
    Header, // The header where the global stuff is defined.
    InterChar, // In between char definitions. Expect STARTCHAR
    CharDef, // In a char definition where the char-specific stuff is defined.
    CharBits, // In a char definition where the bitmap is defined. Expect bitmap data.
    End // End of file. Any more stuff after this throws an exception.
  }

  class BDFSyntaxException(val state: State, val line: Int, val s: String)
    : RuntimeException("Syntax exception on line $line (state $state): $s")
}