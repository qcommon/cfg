package net.dblsaiko.qcommon.cfg.core.net;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.Unpooled;
import net.dblsaiko.qcommon.cfg.core.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;

public class CvarUpdatePacket {

    public static final Identifier PACKET_ID = new Identifier("qcommon-cfg", "cvars");

    private final Map<String, String[]> values;
    private final boolean isSilentUpdate;

    public CvarUpdatePacket(Map<String, String[]> values, boolean isSilentUpdate) {
        this.values = values;
        this.isSilentUpdate = isSilentUpdate;
    }

    public void sendTo(PlayerEntity player) {
        if (values.isEmpty()) return;
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }

    private void onReceive(PacketContext ctx) {
        ctx.getTaskQueue().execute(() -> {
            ConfigApi api = ConfigApi.INSTANCE;
            if (api.lockCvars()) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                values.forEach((key, value) -> {
                    ConVar conVar = api.getConVar(key);
                    if (conVar != null && api.allowRemoteSetCvar(key)) {
                        String oldValue = conVar.getStringRepr();
                        conVar.setFromStrings(value);

                        if (!isSilentUpdate && player != null) {
                            player.addChatMessage(new TranslatableText("qcommon-cfg.cvar_changed", key, oldValue, conVar.getStringRepr()).formatted(Formatting.GOLD), false);
                        }
                    } else {
                        ConfigApi.logger.warn("Server tried setting non-sync cvar '{}'", key);
                    }
                });
            } else {
                // This is expected when joining a singleplayer world.
                ConfigApi.logger.debug("Ignoring cvar sync packet, could not lock remote cvars");
            }
        });
    }

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(values.size());
        values.forEach((key, value) -> {
            buf.writeString(key);
            buf.writeVarInt(value.length);
            Arrays.stream(value).forEach(buf::writeString);
        });
        buf.writeBoolean(isSilentUpdate);
    }

    public static CvarUpdatePacket from(PacketByteBuf buf) {
        Map<String, String[]> values = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int i = 0; i < mapSize; i++) {
            String key = buf.readString();
            int arrSize = buf.readVarInt();
            String[] value = new String[arrSize];
            for (int j = 0; j < arrSize; j++) {
                value[j] = buf.readString();
            }
            values.put(key, value);
        }
        boolean silentUpdate = buf.readBoolean();
        return CvarUpdatePacket.of(values, silentUpdate);
    }

    public static CvarUpdatePacket of(Map<String, String[]> values, boolean silentUpdate) {
        return new CvarUpdatePacket(values, silentUpdate);
    }

    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, (ctx, buf) -> CvarUpdatePacket.from(buf).onReceive(ctx));
    }

}
