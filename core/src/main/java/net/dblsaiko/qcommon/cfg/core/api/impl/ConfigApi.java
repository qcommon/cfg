package net.dblsaiko.qcommon.cfg.core.api.impl;

import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandDispatcher;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;
import net.dblsaiko.qcommon.cfg.core.persistence.PersistenceManager;

public class ConfigApi implements net.dblsaiko.qcommon.cfg.core.api.ConfigApi.Mutable {

    /**
     * The internal {@link ConfigApi} instance.
     * <p>
     * Use {@link net.dblsaiko.qcommon.cfg.core.api.ConfigApi#getInstance()} or
     * {@link net.dblsaiko.qcommon.cfg.core.api.ConfigApi#getInstanceMut()}
     * instead!
     */
    public static final ConfigApi INSTANCE = new ConfigApi();

    private final CombinedLinePrinter output = new CombinedLinePrinter();
    private final CommandRegistry registry = new CommandRegistry();
    private final CommandDispatcher dispatcher = new CommandDispatcher(registry, output);
    private final CvarPersistenceListener cvarPersistenceListener = new CvarPersistenceListener();
    private final PersistenceManager persistenceManager = new PersistenceManager(dispatcher);

    private ConfigApi() {
        Logger logger = LogManager.getLogger("qcommon-cfg");
        output.addListener(logger::info);
        persistenceManager.addListener(cvarPersistenceListener);
        registry.addCommand("save", (args, source, printer, cf) -> persistenceManager.save());
        registry.addCommand("echo", (args, source, printer, cf) -> printer.print(String.join(" ", args)));
        registry.addCommand("exec", new ExecCommand(FabricLoader.getInstance().getConfigDirectory().toPath()));
    }

    @Nullable
    @Override
    public ConVar getConVar(@NotNull String name) {
        return registry.findCvar(name);
    }

    @Nullable
    @Override
    public Command getCommand(@NotNull String name) {
        return registry.findCommand(name);
    }

    @Override
    public void exec(@NotNull String script, @NotNull ExecSource source) {
        dispatcher.exec(script, source);
    }

    @Override
    @NotNull
    public String escape(@NotNull String s) {
        String escaped = s
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace(";", "\\;");

        if (s.contains(" ")) {
            return String.format("\"%s\"", escaped);
        } else {
            return escaped;
        }
    }

    @Override
    public <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar, @NotNull net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions options) {
        registry.addConVar(name, cvar);

        CvarOptions opts = ((CvarOptions) options);

        if (opts.getSavedTo() != null) {
            cvarPersistenceListener.register(name, cvar, opts.getSavedTo());
        }

        if (opts.isSync()) {
            LogManager.getLogger("qcommon-cfg").warn("warning: registering cvar '{}': sync is not supported yet", name);
        }

        return cvar;
    }

    @Override
    public <T extends Command> T addCommand(@NotNull String name, @NotNull T command) {
        registry.addCommand(name, command);
        return command;
    }

    @Override
    public void registerOutput(@NotNull LinePrinter output) {
        this.output.addListener(output);
    }

    @Override
    public void registerPersistenceListener(@NotNull PersistenceListener listener) {
        persistenceManager.addListener(listener);
    }

    public void resumeScripts() {
        dispatcher.resume();
    }

    public void resumeUntilEmpty() {
        dispatcher.resumeUntilEmpty();
    }

    public void loadConfig() {
        persistenceManager.load();
    }

    public void saveConfig() {
        persistenceManager.save();
    }

    public void onLoad() {
        loadConfig();
        resumeUntilEmpty();
        exec("exec autoexec", ExecSource.EVENT);
        resumeUntilEmpty();
        persistenceManager.save();
    }
}
