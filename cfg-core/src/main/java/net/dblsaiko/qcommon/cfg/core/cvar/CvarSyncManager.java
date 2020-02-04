package net.dblsaiko.qcommon.cfg.core.cvar;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.dblsaiko.qcommon.cfg.core.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.sync.SyncListener;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;
import net.dblsaiko.qcommon.cfg.core.net.CvarUpdatePacket;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class CvarSyncManager implements SyncListener {

    private final Set<String> trackedCvars = new HashSet<>();
    private final CommandRegistry registry;

    /**
     * Set to true when the client is connected to a server and receives the
     * cvar sync packet.
     */
    private boolean cvarsLocked = false;

    public CvarSyncManager(CommandRegistry registry) {
        this.registry = registry;
    }

    public boolean isTracked(String cvar) {
        return trackedCvars.contains(cvar);
    }

    public void trackCvar(String cvar) {
        trackedCvars.add(cvar);
    }

    public boolean lockCvars() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT &&
            MinecraftClient.getInstance().getCurrentServerEntry() != null) {
            if (!cvarsLocked) ConfigApi.logger.info("Locking remote cvars");
            cvarsLocked = true;
            return true;
        }
        return false;
    }

    public void unlockCvars() {
        if (cvarsLocked) ConfigApi.logger.info("Unlocking remote cvars");
        cvarsLocked = false;
    }

    public CvarUpdatePacket getFullUpdatePacket() {
        Map<String, String[]> values = trackedCvars.stream()
            .map(cvar -> Pair.of(cvar, Objects.requireNonNull(registry.findCvar(cvar)).getAsStrings()))
            .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
        return CvarUpdatePacket.of(values);
    }

    public CvarUpdatePacket getUpdatePacketFor(String cvar) {
        if (!trackedCvars.contains(cvar)) return CvarUpdatePacket.of(Collections.emptyMap());
        return CvarUpdatePacket.of(Collections.singletonMap(cvar, Objects.requireNonNull(registry.findCvar(cvar)).getAsStrings()));
    }

    @Override
    public void updateAll(@NotNull Set<PlayerEntity> players) {
        CvarUpdatePacket packet = getFullUpdatePacket();
        players.forEach(packet::sendTo);
    }

    public boolean isActive() {
        return cvarsLocked;
    }

    public boolean allowRemoteSetCvar(String cvar) {
        return isActive() && isTracked(cvar);
    }
}
