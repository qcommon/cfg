package therealfarfetchd.commoncfg.api.cmds

interface CvarMapper<T> {
  fun parse(str: String): T
  fun toStr(value: T): String = value.toString()
}

object StringMapper : CvarMapper<String> {
  override fun parse(str: String) = str
}

object IntMapper : CvarMapper<Int> {
  override fun parse(str: String) = str.toIntOrNull() ?: 0
}

object FloatMapper : CvarMapper<Float> {
  override fun parse(str: String) = str.toFloatOrNull() ?: 0.0f
}

object BooleanMapper : CvarMapper<Boolean> {
  override fun parse(str: String) = IntMapper.parse(str) != 0
  override fun toStr(value: Boolean) = if (value) "1" else "0"
}