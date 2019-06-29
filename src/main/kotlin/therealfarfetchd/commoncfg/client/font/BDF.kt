package therealfarfetchd.commoncfg.client.font

import java.util.*

/**
 * An implementation of the BDF (Glyph Bitmap Distribution Format).
 */
data class BDF(
  val fontVer: String = "2.1",
  val glyphs: Map<Char, IGlyph>,
  val defaultGlyph: Char
) {
  interface IGlyph {
    val name: String
    val codePoint: Char
    val width: Int
    val height: Int
    val xOff: Int
    val yOff: Int
    val dwidthX: Int
    val dwidthY: Int
    val bits: BitSet
  }
}