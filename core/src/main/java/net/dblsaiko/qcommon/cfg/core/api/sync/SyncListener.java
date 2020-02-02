package net.dblsaiko.qcommon.cfg.core.api.sync;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Handles server -> client synchronization.
 */
public interface SyncListener {

    /**
     * Called when all state should be synced to the client.
     */
    void updateAll(@NotNull Set<PlayerEntity> players);

}
