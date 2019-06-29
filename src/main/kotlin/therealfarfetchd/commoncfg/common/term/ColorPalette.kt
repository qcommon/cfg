package therealfarfetchd.commoncfg.common.term

class ColorPalette {
  // Taken from Konsole.
  private val colors = intArrayOf(
    // Normal
    0x000000,
    0xB21818,
    0x18B218,
    0xB26818,
    0x1818B2,
    0xB218B2,
    0x18B2B2,
    0xB2B2B2,

    // Light
    0x686868,
    0xFF5454,
    0x54FF54,
    0xFFFF54,
    0x5454FF,
    0xFF54FF,
    0x54FFFF,
    0xFFFFFF,

    // Dark
    0x181818,
    0x650000,
    0x006500,
    0x655E00,
    0x000065,
    0x650065,
    0x006565,
    0x656565
  )

  fun getColor(index: Int, highlight: Highlight = Highlight.Normal) = colors[index + highlight.id * 8]

  enum class Highlight(val id: Int) {
    Normal(0), Light(1), Dark(2);

    companion object {
      fun byId(id: Int) = when (id) {
        Normal.id -> Normal
        Light.id -> Light
        Dark.id -> Dark
        else -> Normal
      }
    }
  }
}