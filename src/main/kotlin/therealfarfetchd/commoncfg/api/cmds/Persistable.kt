package therealfarfetchd.commoncfg.api.cmds

import therealfarfetchd.commoncfg.cmds.impl.Output

interface Persistable {

  val category: String?

  fun write(o: Output)

  companion object {
    fun escape(str: String): String =
      str.replace(Regex("""[\\"]"""), """\\$0""")
        .let { if (" " in str) "\"$it\"" else it }
  }

}