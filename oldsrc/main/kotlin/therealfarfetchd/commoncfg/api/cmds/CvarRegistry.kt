package therealfarfetchd.commoncfg.api.cmds

import therealfarfetchd.commoncfg.api.cmds.CvarRegistry.Mutable
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty0

interface CvarRegistry {

  val cvars: Map<String, ConVar>

  interface Mutable : CvarRegistry {
    @JvmDefault
    fun <T> provide(name: String, field: Field, on: Any, persistFile: String?, mapper: CvarMapper<T>) =
      provide(name, CvarField.from(field, on), persistFile, mapper)

    fun <T> provide(name: String, field: CvarField<T>, persistFile: String?, mapper: CvarMapper<T>): ConVar
  }

  companion object {
    inline fun <reified T> defaultMapper(): CvarMapper<T> {
      return when (T::class) {
        String::class -> StringMapper
        Int::class -> IntMapper
        Float::class -> FloatMapper
        Boolean::class -> BooleanMapper
        else -> error("No default mapper for type '${T::class}'")
      } as CvarMapper<T>
    }
  }

}

inline fun <reified T> Mutable.provide(name: String, field: KMutableProperty0<T>, persistFile: String? = null, mapper: CvarMapper<T> = CvarRegistry.defaultMapper()) =
  provide(name, CvarField.from(field), persistFile, mapper)
