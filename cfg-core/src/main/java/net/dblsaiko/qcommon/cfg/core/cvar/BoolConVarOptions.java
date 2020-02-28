package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.cvar.BoolConVar;
import org.jetbrains.annotations.NotNull;

public class BoolConVarOptions implements BoolConVar.Options {

    private final BoolConVar.Style style;

    private BoolConVarOptions(BoolConVar.Style style) {
        this.style = style;
    }

    @Override
    @NotNull
    public BoolConVar.Options style(BoolConVar.Style style) {
        return new BoolConVarOptions(style);
    }

    public BoolConVar.Style getStyle() {
        return style;
    }

    public static BoolConVarOptions create() {
        return new BoolConVarOptions(BoolConVar.Style.NONZERO);
    }

}
