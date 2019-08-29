package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.Keyboard;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import therealfarfetchd.commoncfg.client.ext.KeyboardExt;

@Mixin(Keyboard.class)
public class KeyboardMixin implements KeyboardExt {

    @Shadow
    private boolean repeatEvents;

    @Override
    public boolean getRepeatEvents() {
        return repeatEvents;
    }

}
