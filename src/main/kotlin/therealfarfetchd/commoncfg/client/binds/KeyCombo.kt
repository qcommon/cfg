package therealfarfetchd.commoncfg.client.binds

data class KeyCombo(val keys: Set<Key>) {

  fun getPrecedence(other: KeyCombo): KeyPrecedence {
    return when {
      keys.none { it in other.keys } -> KeyPrecedence.UNIMPORTANT
      keys.size < other.keys.size -> KeyPrecedence.LESS
      keys.size == other.keys.size -> KeyPrecedence.SAME
      else -> KeyPrecedence.GREATER
    }
  }

  fun fmt(): String {
    return keys.sortedBy { it.sortIndex }.joinToString("+") { it.id }
  }

  companion object {
    fun parse(s: String): KeyCombo? {
      val keys = mutableSetOf<Key>()
      var i = 0
      while (true) {
        val nextSep = s.indexOf('+', i + 1)
        if (nextSep == -1) {
          keys += Key.byId(s.substring(i)) ?: return null
          break
        } else {
          keys += Key.byId(s.substring(i, nextSep)) ?: return null
          i = nextSep + 1
        }
      }
      return KeyCombo(keys)
    }
  }

}

enum class KeyPrecedence {
  GREATER,
  LESS,
  SAME,
  UNIMPORTANT,
}
