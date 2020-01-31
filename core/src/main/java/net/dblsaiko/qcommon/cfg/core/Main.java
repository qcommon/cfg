package net.dblsaiko.qcommon.cfg.core;

import net.fabricmc.api.ModInitializer;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();

        // all of these should be synced with the client and saved to test-config.cfg
        CvarOptions opts = CvarOptions.create().sync().save("test-config");

        IntConVar ownedValue = api.addConVar("owned_value", IntConVar.owned(0), opts);

        int value = ownedValue.get();
        ownedValue.set(420);

        api.addConVar("float_var", FloatConVar.owned(0.25f, FloatConVar.Options.create().min(0.0f).max(10.0f).step(0.1f)));

        api.addConVar("string_var", StringConVar.owned(""), opts);

        api.exec("string_var", ExecSource.CONSOLE);
        api.exec("string_var heyo!", ExecSource.CONSOLE);
        api.exec("string_var", ExecSource.CONSOLE);
    }

}
