package net.dblsaiko.qcommon.cfg.base.ext;

import net.minecraft.client.options.DoubleOption;

public interface DoubleOptionExt {

    double getStep();

    static DoubleOptionExt from(DoubleOption self) {
        return (DoubleOptionExt) self;
    }

}
