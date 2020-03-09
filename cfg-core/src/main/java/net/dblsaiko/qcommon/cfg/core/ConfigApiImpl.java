package net.dblsaiko.qcommon.cfg.core;

import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cmd.Command;
import net.dblsaiko.qcommon.cfg.core.api.cmd.CommandOptions;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import net.dblsaiko.qcommon.cfg.core.api.sync.SyncListener;
import net.dblsaiko.qcommon.cfg.core.cmd.CommandOptionsImpl;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandDispatcher;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;
import net.dblsaiko.qcommon.cfg.core.cvar.CvarOptionsImpl;
import net.dblsaiko.qcommon.cfg.core.cvar.CvarPersistenceListener;
import net.dblsaiko.qcommon.cfg.core.cvar.CvarSyncManager;
import net.dblsaiko.qcommon.cfg.core.persistence.PersistenceManager;
import net.dblsaiko.qcommon.cfg.core.util.CombinedLinePrinter;
import net.dblsaiko.qcommon.cfg.core.util.ExecCommand;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigApiImpl implements ConfigApi.Mutable {

    public static final String MOD_ID = "qcommon-cfg";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    /**
     * The internal {@link ConfigApiImpl} instance.
     * <p>
     * Use {@link ConfigApi#getInstance()} or
     * {@link ConfigApi#getInstanceMut()}
     * instead!
     */
    public static final ConfigApiImpl INSTANCE = new ConfigApiImpl();

    private final CombinedLinePrinter output = new CombinedLinePrinter();
    private final CommandRegistry registry = new CommandRegistry();
    private final CvarSyncManager cvarSyncManager = new CvarSyncManager(registry);
    private final CommandDispatcher dispatcher = new CommandDispatcher(registry, cvarSyncManager, output);
    private final CvarPersistenceListener cvarPersistenceListener = new CvarPersistenceListener();
    private final PersistenceManager persistenceManager = new PersistenceManager(dispatcher);
    private final Set<SyncListener> syncListeners = new HashSet<>();
    private final Map<String, CommandDescription> descriptions = new HashMap<>();
    private final Map<String, CommandDescription> longDescriptions = new HashMap<>();


    private ConfigApiImpl() {
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
    public Map<String, ConVar> getConVars() {
        return registry.getCvars();
    }

    @Override
    public Map<String, Command> getCommands() {
        return registry.getCommands();
    }

    @Override
    public void exec(@NotNull String script, @NotNull ExecSource source) {
        dispatcher.exec(script, source);
    }

    @Override
    public void exec(@NotNull List<List<String>> script, @NotNull ExecSource source) {
        dispatcher.exec(script, source);
    }

    @Override
    public String getDescription(@NotNull String command) {
        CommandDescription desc = descriptions.get(command);
        if (desc == null) return null;
        return desc.getValue(command);
    }

    @Override
    public String getLongDescription(@NotNull String command) {
        CommandDescription desc = longDescriptions.get(command);
        if (desc == null) return null;
        return desc.getValue(command);
    }

    @Override
    @NotNull
    public String escape(@NotNull String s) {
        if (s.isEmpty()) return "\"\"";

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
    @NotNull
    public List<List<String>> tokenize(@NotNull String script) {
        return CommandDispatcher.tokenize(script);
    }

    @Override
    public <T extends ConVar> T addConVar(@NotNull String name, @NotNull T cvar, @NotNull CvarOptions options) {
        CvarOptionsImpl opts = ((CvarOptionsImpl) options);

        registry.addConVar(name, cvar);

        if (opts.getSavedTo() != null) {
            cvarPersistenceListener.register(name, cvar, opts.getSavedTo());
        }

        descriptions.put(name, opts.getDesc());
        longDescriptions.put(name, opts.getExtendedDesc());

        if (opts.isSync()) {
            cvarSyncManager.trackCvar(name);
        }

        return cvar;
    }

    @Override
    public <T extends Command> T addCommand(@NotNull String name, @NotNull T command, @NotNull CommandOptions options) {
        CommandOptionsImpl opts = ((CommandOptionsImpl) options);

        registry.addCommand(name, command);

        descriptions.put(name, opts.getDesc());
        longDescriptions.put(name, opts.getExtendedDesc());

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
        syncListeners.forEach($ -> $.updateAll(Collections.singleton(player)));
    }

    public void onLoad() {
        loadConfig();
        resumeUntilEmpty();
        exec("exec autoexec", ExecSource.EVENT);
        resumeUntilEmpty();
        persistenceManager.save();
    }

    public boolean lockCvars() {
        return cvarSyncManager.lockCvars();
    }

    public void unlockCvars() {
        cvarSyncManager.unlockCvars();
    }

    public boolean allowRemoteSetCvar(String cvar) {
        return cvarSyncManager.allowRemoteSetCvar(cvar);
    }

}
