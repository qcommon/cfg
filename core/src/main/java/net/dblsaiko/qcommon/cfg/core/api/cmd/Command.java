package net.dblsaiko.qcommon.cfg.core.api.cmd;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import org.jetbrains.annotations.NotNull;

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
     * @param cf     the {@link ControlFlow} instance
     */
    void exec(@NotNull String[] args, @NotNull ExecSource src, @NotNull LinePrinter output, @NotNull ControlFlow cf);

}
