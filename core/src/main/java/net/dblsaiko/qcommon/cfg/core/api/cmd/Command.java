package net.dblsaiko.qcommon.cfg.core.api.cmd;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

public interface Command {

    void exec(@NotNull String[] args, @NotNull ExecSource src, @NotNull ConsoleOutput output);

}
