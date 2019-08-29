package therealfarfetchd.commoncfg.client.ext

import net.minecraft.client.options.DoubleOption

interface DoubleOptionExt {

  val interval: Double

}

val DoubleOption.interval
  get() = (this as DoubleOptionExt).interval