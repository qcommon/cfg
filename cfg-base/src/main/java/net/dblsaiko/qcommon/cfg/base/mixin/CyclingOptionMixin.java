package net.dblsaiko.qcommon.cfg.base.mixin;

import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.CyclingOption.Setter;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.dblsaiko.qcommon.cfg.base.ext.CyclingOptionExt;

@Mixin(CyclingOption.class)
public class CyclingOptionMixin<T> implements CyclingOptionExt<T> {
    @Shadow
    @Final
    private Setter<T> setter;

    @Override
    public Setter<T> getSetter() {
        return this.setter;
    }
}
