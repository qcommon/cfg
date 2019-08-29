package therealfarfetchd.commoncfg.api.cmds

import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty0

interface CvarField<T> : () -> T {

  fun set(value: T)

  companion object {
    fun <T> from(prop: KMutableProperty0<T>) = object : CvarField<T> {
      override fun invoke() = prop()
      override fun set(value: T) = prop.set(value)
    }

    fun <T> from(f: Field, on: Any): CvarField<T> {
      val getter = MethodHandles.lookup().unreflectGetter(f).bindTo(on)
      val setter = MethodHandles.lookup().unreflectSetter(f).bindTo(on)

      return object : CvarField<T> {
        @Suppress("UNCHECKED_CAST")
        override fun invoke(): T = getter.invokeWithArguments() as T

        override fun set(value: T) {
          setter.invokeWithArguments(value)
        }
      }
    }

    fun <T> from(getter: () -> T, setter: (T) -> Unit): CvarField<T> = object : CvarField<T> {
      override fun invoke(): T = getter()
      override fun set(value: T) = setter(value)
    }
  }

}