package net.dblsaiko.qcommon.cfg.core.api.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandDispatcher;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;

public class ConfigApi implements net.dblsaiko.qcommon.cfg.core.api.ConfigApi.Mutable {

    public static final ConfigApi INSTANCE = new ConfigApi();

    private final CombinedConsoleOutput output = new CombinedConsoleOutput();
    private final CommandRegistry registry = new CommandRegistry();
    private final CommandDispatcher dispatcher = new CommandDispatcher(registry, output);

    private ConfigApi() {
        Logger logger = LogManager.getLogger("qcommon-cfg");
        output.addListener(logger::info);
    }

    @NotNull
    @Override
    public ConVar getConVar(String name) {
        return registry.findCvar(name);
    }

    @NotNull
    @Override
    public Command getCommand(String name) {
        return registry.findCommand(name);
    }

    @Override
    public void exec(@NotNull String script, @NotNull ExecSource source) {
        dispatcher.exec(script, source);
    }

    @Override
    public <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar, @NotNull CvarOptions options) {
        registry.addConVar(name, cvar);
        return cvar;
    }

    @Override
    public <T extends Command> T addCommand(@NotNull String name, @NotNull T command) {
        registry.addCommand(name, command);
        return command;
    }

    @Override
    public void registerOutput(@NotNull ConsoleOutput output) {
        this.output.addListener(output);
    }

    public void resumeScripts() {
        dispatcher.resume();
    }

}
