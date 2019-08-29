package therealfarfetchd.commoncfg.client.binds

import net.minecraft.client.options.KeyBinding

interface KeyBindingExt {

  fun setPressed(value: Boolean)

  fun incrTimesPressed()

}

fun KeyBinding.setPressed(value: Boolean) =
  (this as KeyBindingExt).setPressed(value)

fun KeyBinding.incrTimesPressed() =
  (this as KeyBindingExt).incrTimesPressed()