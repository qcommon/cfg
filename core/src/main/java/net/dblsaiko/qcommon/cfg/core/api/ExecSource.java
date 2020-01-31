package net.dblsaiko.qcommon.cfg.core.api;

import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;

/**
 * The execution source for a script. Determines whether
 */
public enum ExecSource {

    /**
     * The script has been executed from the ingame console.
     */
    CONSOLE,

    /**
     * The script has been executed because of some event, e.g. executing
     * autoexec.cfg after game start.
     */
    EVENT,

    /**
     * The script has been executed from the server, such as for synchronizing
     * cvars.
     * Since this is effectively arbitrary code execution, this command source
     * has an extra security check that is disabled by default, so that every
     * command doesn't have to implement that check itself.
     *
     * @see Command#allowRemoteExec()
     */
    REMOTE,

    /**
     * The script has been executed because of a key press.
     */
    KEY,

}
