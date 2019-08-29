package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.options.KeyBinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import therealfarfetchd.commoncfg.client.binds.KeyBindingExt;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements KeyBindingExt {

    @Shadow
    private boolean pressed;

    @Shadow
    private int timesPressed;

    @Overwrite
    public static void updatePressedStates() {
        // TODO: prevent this from getting called
    }

    @Override
    public void setPressed(boolean value) {
        this.pressed = value;
    }

    @Override
    public void incrTimesPressed() {
        this.timesPressed++;
    }
}
