package net.dblsaiko.qcommon.cfg.core.api;

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
     * The script has been executed because of a key press.
     */
    KEY,

    /**
     * The script has been executed because of some UI action.
     */
    UI,

}
