package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.gui.screen.ConnectScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.commoncfg.CfgLoadTriggersClient;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {

    @Inject(method = "method_2130(Ljava/lang/String;I)V", at = @At("HEAD"))
    private void method_2130(String string_1, int int_1, CallbackInfo ci) {
        CfgLoadTriggersClient.INSTANCE.onOpenMPWorld(string_1, int_1);
    }

}
