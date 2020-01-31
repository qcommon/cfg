package therealfarfetchd.commoncfg.cmds.impl

import org.apache.logging.log4j.Logger

interface Output {
  fun println(s: String)
}

object NullOutput : Output {
  override fun println(s: String) {
  }
}

class LogOutput(val logger: Logger) : Output {
  override fun println(s: String) {
    logger.info(s)
  }
}

class MultiOutput(vararg val outputs: Output) : Output {
  override fun println(s: String) {
    for (output in outputs) {
      output.println(s)
    }
  }
}

class ListOutput(val list: MutableList<String>) : Output {
  override fun println(s: String) {
    list += s
  }
}