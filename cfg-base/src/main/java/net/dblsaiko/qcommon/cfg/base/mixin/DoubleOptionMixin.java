package net.dblsaiko.qcommon.cfg.base.mixin;

import net.dblsaiko.qcommon.cfg.base.ext.DoubleOptionExt;
import net.minecraft.client.options.DoubleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DoubleOption.class)
public abstract class DoubleOptionMixin implements DoubleOptionExt {

    @Shadow
    @Final
    protected float step;

    @Override
    public double getStep() {
        return step;
    }

}
