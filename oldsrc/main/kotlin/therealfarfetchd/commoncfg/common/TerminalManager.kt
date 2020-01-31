package therealfarfetchd.commoncfg.common

import therealfarfetchd.commoncfg.CommonCfg
import therealfarfetchd.commoncfg.api.CommonCfgApi
import therealfarfetchd.commoncfg.api.cmds.ExecSource.CommandLine
import therealfarfetchd.commoncfg.cmds.impl.Output
import therealfarfetchd.commoncfg.common.term.Terminal
import therealfarfetchd.commoncfg.common.term.moveCursor
import therealfarfetchd.commoncfg.common.term.newLine

class TerminalManager(val term: Terminal) : Output {

  var outX = 0

  val currentLine = StringBuilder()

  init {
    setupPrompt()
  }

  fun update() {
    while (true) {
      val (c, _) = term.read() ?: return

      when (c) {
        '\r' -> {
          if (currentLine.isNotEmpty()) {
            if (term.cursorX() == 0) {
              term.cursorY(term.cursorY() - 1)
              term.cursorX(term.width() - 1)
            } else {
              term.cursorX(term.cursorX() - 1)
            }
            term.put(term.cursorX(), term.cursorY(), ' ')
            currentLine.deleteCharAt(currentLine.length - 1)
          }
        }
        '\n' -> {
          term.newLine()
          setupPrompt()
          CommonCfgApi.instance.dispatcher.exec(currentLine.toString(), CommandLine)
          currentLine.clear()
        }
        else -> {
          writeChar(c)
          currentLine.append(c)
        }
      }
    }
  }

  override fun println(s: String) {
    incLine()
    for (c in s) writeCharOut(c)
  }

  fun setupPrompt() {
    writeChar(']')
    writeChar(' ')
  }

  fun writeCharOut(c: Char) {
    if (term.cursorY() == 0) incLine()
    term.put(outX, term.cursorY() - 1, c)
    outX += 1
    if (outX > term.width()) {
      incLine()
    }
  }

  fun incLine() {
    var cy = term.cursorY()
    if (term.cursorY() + 1 < term.height()) {
      term.cursorY(cy + 1)
    } else {
      term.scroll()
      cy--
    }
    for (i in 0 until term.width()) {
      term.put(i, cy + 1, term.get(i, cy) ?: ' ')
      term.put(i, cy, ' ')
    }

    outX = 0
  }

  fun writeChar(c: Char) {
    term.put(term.cursorX(), term.cursorY(), c)
    term.moveCursor()
  }

}