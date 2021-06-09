package net.dblsaiko.qcommon.cfg.keys.mixin;

import net.dblsaiko.qcommon.cfg.keys.ext.KeyBindingExt;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements KeyBindingExt {

    @Shadow
    private int timesPressed;

    @Override
    public void incrTimesPressed() {
        timesPressed += 1;
    }

}
