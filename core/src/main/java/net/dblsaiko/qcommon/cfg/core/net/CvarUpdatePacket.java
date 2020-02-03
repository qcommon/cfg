package net.dblsaiko.qcommon.cfg.core.net;

import io.netty.buffer.Unpooled;
import net.dblsaiko.qcommon.cfg.core.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CvarUpdatePacket {

    public static final Identifier PACKET_ID = new Identifier("qcommon-cfg", "cvars");

    private final Map<String, String[]> values;

    public CvarUpdatePacket(Map<String, String[]> values) {
        this.values = values;
    }

    public void sendTo(PlayerEntity player) {
        if (values.isEmpty()) return;
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }

    private void onReceive(PacketContext ctx) {
        ConfigApi api = ConfigApi.INSTANCE;
        values.forEach((key, value) -> {
            ConVar conVar = api.getConVar(key);
            if (conVar != null && api.allowRemoteSetCvar(key)) {
                String[] oldValue = conVar.getAsStrings();
                conVar.setFromStrings(value);

                String oldStr = Arrays.stream(oldValue).map(ConfigApi.INSTANCE::escape).collect(Collectors.joining(" "));
                String newStr = Arrays.stream(conVar.getAsStrings()).map(ConfigApi.INSTANCE::escape).collect(Collectors.joining(" "));
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.addChatMessage(new TranslatableText("qcommon-cfg.cvar_changed", key, oldStr, newStr), false);
                }
            } else {
                ConfigApi.logger.warn("server tried setting non-sync cvar '{}'", key);
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
        return CvarUpdatePacket.of(values);
    }

    public static CvarUpdatePacket of(Map<String, String[]> values) {
        return new CvarUpdatePacket(values);
    }

    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, (ctx, buf) -> CvarUpdatePacket.from(buf).onReceive(ctx));
    }

}
