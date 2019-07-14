package therealfarfetchd.commoncfg.binds

data class KeyCombo(val keys: Set<Int>) {

  fun getPrecedence(other: KeyCombo): KeyPrecedence {
    return when {
      keys.none { it in other.keys } -> KeyPrecedence.UNIMPORTANT
      keys.size < other.keys.size -> KeyPrecedence.LESS
      keys.size == other.keys.size -> KeyPrecedence.SAME
      else -> KeyPrecedence.GREATER
    }
  }

}

enum class KeyPrecedence {
  GREATER,
  LESS,
  SAME,
  UNIMPORTANT,
}
