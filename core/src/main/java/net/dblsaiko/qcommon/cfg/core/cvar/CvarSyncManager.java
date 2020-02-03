package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.sync.SyncListener;
import net.dblsaiko.qcommon.cfg.core.cmdproc.CommandRegistry;
import net.dblsaiko.qcommon.cfg.core.net.CvarUpdatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CvarSyncManager implements SyncListener {

    private final Set<String> trackedCvars = new HashSet<>();
    private final CommandRegistry registry;

    public CvarSyncManager(CommandRegistry registry) {
        this.registry = registry;
    }

    public boolean isTracked(String cvar) {
        return trackedCvars.contains(cvar);
    }

    public void trackCvar(String cvar) {
        trackedCvars.add(cvar);
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
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            // TODO also check if the server even has cfg installed!
            return MinecraftClient.getInstance().getCurrentServerEntry() != null;
        }
        return false;
    }

}
