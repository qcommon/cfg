package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;

/**
 * A console variable (convar/cvar) which stores a modifiable value for configuration.
 *
 * @see IntConVar
 * @see FloatConVar
 * @see StringConVar
 */
public interface ConVar {

    /**
     * Sets this ConVar's value from the passed arguments.
     *
     * @param args the arguments
     */
    void setFromString(@NotNull String[] args);

    /**
     * Prints this ConVar's state to the output
     *
     * @param name   the name of the cvar
     * @param output the output to print to
     */
    void printState(@NotNull String name, @NotNull ConsoleOutput output);

}
