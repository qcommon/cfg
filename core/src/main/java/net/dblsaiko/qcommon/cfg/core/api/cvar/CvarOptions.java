package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

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
     * @return this
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
     * @return this
     */
    @NotNull
    CvarOptions save(@NotNull String file);

    /**
     * Create a new {@link CvarOptions} instance with default settings
     * (no sync, no save)
     *
     * @return a new {@link CvarOptions} instance
     */
    @NotNull
    static CvarOptions create() {
        return new net.dblsaiko.qcommon.cfg.core.cvar.CvarOptions();
    }

}
