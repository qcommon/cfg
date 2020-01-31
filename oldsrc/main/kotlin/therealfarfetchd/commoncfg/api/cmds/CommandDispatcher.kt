package therealfarfetchd.commoncfg.api.cmds

import therealfarfetchd.commoncfg.api.cmds.ExecSource.Unknown

interface CommandDispatcher {

  fun exec(line: String, source: ExecSource = Unknown)

  fun execFile(file: String)

}