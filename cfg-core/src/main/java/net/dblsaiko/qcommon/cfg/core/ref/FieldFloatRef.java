package net.dblsaiko.qcommon.cfg.core.ref;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;

import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FieldFloatRef implements FloatRef {

    private final MethodHandle getter;
    private final MethodHandle setter;

    public FieldFloatRef(@NotNull Field field, @Nullable Object target) {
        if (field.getType() != float.class) {
            throw new IllegalArgumentException(String.format("Field %s is not of type float!", field));
        }
        Lookup mh = MethodHandles.lookup();
        try {
            this.getter = mh.unreflectGetter(field).bindTo(target);
            this.setter = mh.unreflectSetter(field).bindTo(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float get() {
        try {
            return (float) getter.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void set(float value) {
        try {
            setter.invoke(value);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
