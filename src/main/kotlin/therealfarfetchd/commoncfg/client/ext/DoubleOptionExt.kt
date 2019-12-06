package therealfarfetchd.commoncfg.client.ext

import net.minecraft.client.options.DoubleOption

interface DoubleOptionExt {

  val step: Double

}

val DoubleOption.step
  get() = (this as DoubleOptionExt).step