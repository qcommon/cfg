package net.dblsaiko.qcommon.cfg.core.api.cvar;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import org.jetbrains.annotations.NotNull;

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
    void setFromStrings(@NotNull String[] args);

    /**
     * Gets this ConVar's value as an array of strings for serialization.
     *
     * @return this cvar's value as a String[]
     * @see ConVar#setFromStrings(String[])
     */
    @NotNull
    String[] getAsStrings();

    /**
     * Get a user readable string representation of this value.
     *
     * @return the string representation
     */
    @NotNull
    String getStringRepr();

    /**
     * Prints this ConVar's state to the output
     *
     * @param name   the name of the cvar
     * @param output the output to print to
     */
    void printState(@NotNull String name, @NotNull LinePrinter output);

}
