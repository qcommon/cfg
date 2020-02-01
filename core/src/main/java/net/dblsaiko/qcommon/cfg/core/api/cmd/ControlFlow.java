package net.dblsaiko.qcommon.cfg.core.api.cmd;

/**
 * Interface to allow controlling the script's control flow.
 */
public interface ControlFlow {

    /**
     * Proceed execution of the script. This is the default setting.
     */
    void proceed();

    /**
     * Suspend execution of the script until the next tick.
     */
    void suspend();

    /**
     * Enter a subroutine.
     *
     * @param script the source of the subroutine
     */
    void enterSubroutine(String script);

}
