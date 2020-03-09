package net.dblsaiko.qcommon.cfg.core.api;

import net.dblsaiko.qcommon.cfg.core.ConfigApiImpl;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cmd.CommandOptions;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import net.dblsaiko.qcommon.cfg.core.api.sync.SyncListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * The central API for qcommon-cfg.
 */
public interface ConfigApi {

    /**
     * Get the {@link ConfigApi} instance.
     *
     * @return the instance
     */
    @NotNull
    static ConfigApi getInstance() {
        return ConfigApiImpl.INSTANCE;
    }

    /**
     * Get a mutable version of the {@link ConfigApi} instance
     *
     * @return c
     */
    @NotNull
    static ConfigApi.Mutable getInstanceMut() {
        return ConfigApiImpl.INSTANCE;
    }

    /**
     * Get a cvar by name.
     *
     * @param name the name of the cvar
     * @return the cvar, or null if no cvar by this name exists
     */
    @Nullable
    ConVar getConVar(@NotNull String name);

    /**
     * Get a command by name
     *
     * @param name the name of the command
     * @return the command, or null if no command by this name exists
     */
    @Nullable
    Command getCommand(@NotNull String name);

    /**
     * Get a map containing all registered cvars.
     *
     * @return the map of all cvars
     */
    Map<String, ConVar> getConVars();

    /**
     * Get a map containing all registered commands.
     *
     * @return the map of all commands
     */
    Map<String, Command> getCommands();

    /**
     * Schedule a script for execution. The script will be executed on the next tick.
     *
     * @param script the script to execute
     * @param source the source this script should be executed from
     */
    void exec(@NotNull String script, @NotNull ExecSource source);

    /**
     * Schedule an already parsed script for execution. The script will be
     * executed on the next tick.
     *
     * @param script the script to execute
     * @param source the source this script should be executed from
     */
    void exec(@NotNull List<List<String>> script, @NotNull ExecSource source);

    /**
     * Get the description for the given command or cvar name
     *
     * @param command the command or cvar name
     * @return the description, or <code>null</code> if no command or cvar by that name exists
     * @see CvarOptions#desc(CommandDescription)
     */
    @Nullable
    String getDescription(@NotNull String command);

    /**
     * Get the long description for the given command or cvar name
     *
     * @param command the command or cvar name
     * @return the long description, or <code>null</code> if no command or cvar by that name exists
     * @see CvarOptions#extendedDesc(CommandDescription)
     */
    @Nullable
    String getLongDescription(@NotNull String command);

    /**
     * Escapes a string so that when parsed, it evaluates to the original string.
     *
     * @param s the string to escape
     * @return the escaped string
     */
    @NotNull
    String escape(@NotNull String s);

    /**
     * Tokenize a script into a list of commands, where each command is a list
     * of arguments (first element is the actual command name itself)
     *
     * @param script the script to tokenize
     * @return the tokenized script
     */
    @NotNull
    List<List<String>> tokenize(@NotNull String script);

    /**
     * Mutable extension of {@link ConfigApi}, for registration.
     */
    interface Mutable extends ConfigApi {

        /**
         * Add a new ConVar.
         *
         * @param name the name of the cvar to add
         * @param cvar the cvar to add
         * @param <T>  the cvar type
         * @return the added cvar
         * @throws IllegalStateException if a cvar by that name already exists.
         */
        @NotNull
        default <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar) {
            return addConVar(name, cvar, CvarOptions.create());
        }

        /**
         * Add a new ConVar.
         *
         * @param name    the name of the cvar to add
         * @param cvar    the cvar to add
         * @param options the options to add this cvar with
         * @param <T>     the cvar type
         * @return the added cvar
         * @throws IllegalStateException if a cvar by that name already exists.
         */
        <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar, @NotNull CvarOptions options);

        /**
         * Add a new command.
         *
         * @param name    the name of the command to add
         * @param command the command to add
         * @param <T>     the command type
         * @return the added command
         */
        default <T extends Command> T addCommand(@NotNull String name, @NotNull T command) {
            return addCommand(name, command, CommandOptions.create());
        }

        /**
         * Add a new command.
         *
         * @param name    the name of the command to add
         * @param command the command to add
         * @param options the options to add this command with
         * @param <T>     the command type
         * @return the added command
         */
        <T extends Command> T addCommand(@NotNull String name, @NotNull T command, @NotNull CommandOptions options);

        /**
         * Register a {@link LinePrinter} that all commands print their output
         * to, in addition to the currently registered ones.
         *
         * @param output the {@link LinePrinter} to register
         */
        void registerOutput(@NotNull LinePrinter output);

        /**
         * Register a {@link PersistenceListener}.
         *
         * @param listener the {@link PersistenceListener} to register
         */
        void registerPersistenceListener(@NotNull PersistenceListener listener);

        /**
         * Register a {@link SyncListener}.
         *
         * @param listener the sync listener to register
         */
        void registerSyncListener(@NotNull SyncListener listener);

    }

}
