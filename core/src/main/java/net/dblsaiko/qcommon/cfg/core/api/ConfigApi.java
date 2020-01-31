package net.dblsaiko.qcommon.cfg.core.api;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;

public interface ConfigApi {

    @NotNull
    static ConfigApi getInstance() {
        return net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi.INSTANCE;
    }

    @NotNull
    static ConfigApi.Mutable getInstanceMut() {
        return net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi.INSTANCE;
    }

    @NotNull
    ConVar getConVar(String name);

    @NotNull
    Command getCommand(String name);

    void exec(@NotNull String script, @NotNull ExecSource source);

    interface Mutable extends ConfigApi {

        @NotNull
        default <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar) {
            return addConVar(name, cvar, CvarOptions.create());
        }

        <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar, @NotNull CvarOptions options);

        <T extends Command> T addCommand(@NotNull String name, @NotNull T command);

        void registerOutput(@NotNull ConsoleOutput output);

    }

}
