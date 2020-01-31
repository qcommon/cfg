package therealfarfetchd.commoncfg.api.cmds

import therealfarfetchd.commoncfg.cmds.impl.Output

interface Persistable {

  val category: String?

  fun write(o: Output)

  companion object {
    fun escape(str: String) = str.replace(Regex("""[\\\s"]"""), """\\$0""")
  }

}