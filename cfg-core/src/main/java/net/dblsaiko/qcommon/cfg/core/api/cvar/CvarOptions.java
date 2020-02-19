package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.cvar.CvarOptionsImpl;

/**
 * Options for cvar registration.
 */
public interface CvarOptions {

    /**
     * Enable server -> client syncing for this cvar. When playing on a
     * dedicated server, clients will not be able to modify this variable, and
     * in case of any modifications from the server, the new value will be sent
     * to all connected clients.
     *
     * @return a new {@link CvarOptions} instance with this option changed
     */
    @NotNull
    CvarOptions sync();

    /**
     * Enable saving to disk for this cvar. Cvars will be saved on game
     * shutdown or when the user executes the <code>save</code> command.
     * <p>
     * When syncing is enabled for this cvar, the old value will be reloaded
     * when the player leaves the server.
     *
     * @param file the file to save this to, will end up as config/{file}.cfg
     * @return a new {@link CvarOptions} instance with this option changed
     */
    @NotNull
    CvarOptions save(@NotNull String file);

    /**
     * Set the description for this cvar. This will show up in help texts and
     * in generated configuration files.
     * <p>
     * The default value is <code>translated("cfg.desc.${cvar}")</code>
     *
     * @param description the description to set
     * @return a new {@link CvarOptions} instance with this option changed
     */
    CvarOptions desc(@NotNull CommandDescription description);

    /**
     * Set the extended description for this cvar. This can span across
     * multiple lines and should explain what this cvar and the available
     * values do in detail. It will show up as a comment above configuration
     * files.
     * <p>
     * The default value is <code>translated("cfg.desc.${cvar}.ext")</code>
     *
     * @param description the description to set
     * @return a new {@link CvarOptions} instance with this option changed
     */
    CvarOptions extendedDesc(@NotNull CommandDescription description);

    /**
     * Create a new {@link CvarOptions} instance with default settings
     * (no sync, no save)
     *
     * @return a new {@link CvarOptions} instance
     */
    @NotNull
    static CvarOptions create() {
        return CvarOptionsImpl.create();
    }

}
