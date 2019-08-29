package therealfarfetchd.commoncfg.client.ext

import net.minecraft.client.Keyboard

interface KeyboardExt {

  val repeatEvents: Boolean

}

val Keyboard.repeatEvents
  get() = (this as KeyboardExt).repeatEvents