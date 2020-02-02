package net.dblsaiko.qcommon.cfg.core.api.impl;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import net.dblsaiko.qcommon.cfg.core.api.sync.SyncListener;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandDispatcher;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;
import net.dblsaiko.qcommon.cfg.core.persistence.PersistenceManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConfigApi implements net.dblsaiko.qcommon.cfg.core.api.ConfigApi.Mutable {

    public static final String MOD_ID = "qcommon-cfg";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

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
    private final CvarSyncManager cvarSyncManager = new CvarSyncManager(registry);
    private final CommandDispatcher dispatcher = new CommandDispatcher(registry, cvarSyncManager, output);
    private final CvarPersistenceListener cvarPersistenceListener = new CvarPersistenceListener();
    private final PersistenceManager persistenceManager = new PersistenceManager(dispatcher);
    private final Set<SyncListener> syncListeners = new HashSet<>();

    private ConfigApi() {
        output.addListener(logger::info);
        persistenceManager.addListener(cvarPersistenceListener);
        registerSyncListener(cvarSyncManager);
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
            .replace("\"", "\\\"");

        if (s.contains(" ") || s.contains(";")) {
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
            cvarSyncManager.trackCvar(name);
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

    @Override
    public void registerSyncListener(@NotNull SyncListener listener) {
        syncListeners.add(listener);
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

    public void onPlayerConnect(ServerPlayerEntity player) {
        cvarSyncManager.getFullUpdatePacket().sendTo(player);
        syncListeners.forEach($ -> $.updateAll(Collections.singleton(player)));
    }

    public void onLoad() {
        loadConfig();
        resumeUntilEmpty();
        exec("exec autoexec", ExecSource.EVENT);
        resumeUntilEmpty();
        persistenceManager.save();
    }

    public boolean allowRemoteSetCvar(String key) {
        return cvarSyncManager.isActive() && cvarSyncManager.isTracked(key);
    }

}
