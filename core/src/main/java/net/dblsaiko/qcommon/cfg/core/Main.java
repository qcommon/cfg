package net.dblsaiko.qcommon.cfg.core;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Main implements ModInitializer {

    private static int myVal = 20;

    @Override
    public void onInitialize() {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();

        // all of these should be synced with the client and saved to test-config.cfg
        CvarOptions opts = CvarOptions.create().sync().save("test-config");

        IntConVar ownedValue = api.addConVar("owned_value", IntConVar.owned(0), opts);

        int value = ownedValue.get();
        ownedValue.set(420);

        api.addConVar("float_var", FloatConVar.owned(0.25f, FloatConVar.Options.create().min(0.0f).max(10.0f).step(0.1f)), opts);

        // either let the cvar store the value
        api.addConVar("string_var", StringConVar.owned(""), opts);

        // or store it externally
        api.addConVar("my_val", IntConVar.wrap(IntRef.from(() -> myVal, it -> myVal = it)), opts);

        api.addCommand("up", (args, src, output, cf) -> {
            Vec3d pos = MinecraftClient.getInstance().player.getPos();
            MinecraftClient.getInstance().player.setPos(pos.x, pos.y + Double.parseDouble(args[0]), pos.z);
        });
    }

}
