package net.dblsaiko.qcommon.cfg.core.api.cmd;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

/**
 * An executable console command.
 */
public interface Command {

    /**
     * Execute this command.
     *
     * @param args   the arguments given to this command, not including the command name itself.
     * @param src    the ExecSource this command was executed from.
     * @param output the output wrapper
     */
    void exec(@NotNull String[] args, @NotNull ExecSource src, @NotNull ConsoleOutput output);

    /**
     * Whether this command may be executed remotely by the server. Since this
     * can pose a security risk, this is disabled by default
     *
     * @return whether this command can be executed remotely
     */
    default boolean allowRemoteExec() {
        return false;
    }

}
