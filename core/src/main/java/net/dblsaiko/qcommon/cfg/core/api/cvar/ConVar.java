package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;

public interface ConVar {

    void setFromString(@NotNull String[] args);

    void printState(@NotNull String name, @NotNull ConsoleOutput output);

}
