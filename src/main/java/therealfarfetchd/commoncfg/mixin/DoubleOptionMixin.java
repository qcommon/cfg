package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.options.DoubleOption;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import therealfarfetchd.commoncfg.client.ext.DoubleOptionExt;

@Mixin(DoubleOption.class)
public class DoubleOptionMixin implements DoubleOptionExt {

    @Final @Shadow
    protected float interval;

    @Override
    public double getInterval() {
        return interval;
    }

}
