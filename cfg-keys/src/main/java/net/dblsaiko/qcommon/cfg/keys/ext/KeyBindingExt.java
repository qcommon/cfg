package net.dblsaiko.qcommon.cfg.keys.ext;

import net.minecraft.client.option.KeyBinding;

public interface KeyBindingExt {

    void incrTimesPressed();

    static KeyBindingExt from(KeyBinding self) {
        return (KeyBindingExt) self;
    }

}
