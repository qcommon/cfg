package net.dblsaiko.qcommon.cfg.base.ext;

import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.CyclingOption.Setter;

public interface CyclingOptionExt<T> {
    Setter<T> getSetter();

    static <T> CyclingOptionExt<T> from(CyclingOption<T> self) {
        // noinspection unchecked
        return (CyclingOptionExt<T>) self;
    }
}
