package therealfarfetchd.commoncfg.api.cmds

import kotlin.math.round

interface CvarMapper<T> {
  fun parse(str: String): T?
  fun toStr(value: T): String = value.toString()
}

object StringMapper : CvarMapper<String> {
  override fun parse(str: String) = str
}

object IntMapper : CvarMapper<Int> {
  override fun parse(str: String) = str.toIntOrNull()
}

object FloatMapper : CvarMapper<Float> {
  override fun parse(str: String) = str.toFloatOrNull()
}

object BooleanMapper : CvarMapper<Boolean> {
  override fun parse(str: String) = IntMapper.parse(str)?.let { it != 0 }
  override fun toStr(value: Boolean) = if (value) "1" else "0"
}

class DoubleMapper private constructor(val lower: Double?, val upper: Double?, val step: Double) : CvarMapper<Double> {
  override fun parse(str: String): Double? {
    var v = str.toDoubleOrNull() ?: return null
    if (step > 0.0) v = (v - round((lower ?: 0.0)) / step) * step + (lower ?: 0.0)
    if (lower != null && v < lower) v = lower
    if (upper != null && v > upper) v = upper
    return v
  }

  companion object {
    val Default = DoubleMapper(null, null, 0.0)
    fun limit(lower: Double, upper: Double, step: Double = 0.0) = DoubleMapper(lower, upper, step)
  }
}