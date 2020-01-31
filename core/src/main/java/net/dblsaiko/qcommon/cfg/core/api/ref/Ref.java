package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.ref.CustomRef;
import net.dblsaiko.qcommon.cfg.core.api.impl.ref.FieldRef;

public interface Ref<T> {

    T get();

    void set(T value);

    @NotNull
    static <T> Ref<T> from(@NotNull Field field, @Nullable Object target) {
        return new FieldRef<>(field, target);
    }

    @NotNull
    static <T> Ref<T> from(@NotNull Supplier<T> getter, @NotNull Consumer<T> setter) {
        return new CustomRef<>(getter, setter);
    }

}
