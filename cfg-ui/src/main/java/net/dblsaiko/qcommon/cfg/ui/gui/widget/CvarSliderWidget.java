package net.dblsaiko.qcommon.cfg.ui.gui.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar;

import static net.dblsaiko.qcommon.cfg.core.util.ArrayUtil.arrayOf;

public class CvarSliderWidget extends SliderWidget {

    private final String name;
    private final ConVar cvar;
    private final float min;
    private final float max;

    protected CvarSliderWidget(String cvar, int x, int y, int width, int height, float min, float max) {
        super(x, y, width, height, LiteralText.EMPTY, getCvarValue(cvar));
        this.cvar = Objects.requireNonNull(ConfigApi.getInstance().getConVar(cvar));
        this.name = cvar;
        this.min = min;
        this.max = max;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new LiteralText(String.format("%s: %s", name,
            Arrays.stream(cvar.getAsStrings())
                .map(s -> ConfigApi.getInstance().escape(s))
                .collect(Collectors.joining(" ")))));
    }

    @Override
    protected void applyValue() {
        cvar.setFromStrings(arrayOf(Float.toString((float) value * max - min) + min));
    }

    private static float getCvarValue(String name) {
        ConVar cvar = ConfigApi.getInstance().getConVar(name);
        if (cvar == null) return 0.0f;
        return getCvarValue(cvar);
    }

    private static float getCvarValue(ConVar cvar) {
        String[] v = cvar.getAsStrings();
        if (v.length != 1) return 0.0f;
        try {
            return Float.parseFloat(v[0]);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

}
