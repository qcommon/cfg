package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.ref.CustomIntRef;
import net.dblsaiko.qcommon.cfg.core.api.impl.ref.FieldIntRef;

public interface IntRef {

    int get();

    void set(int value);

    @NotNull
    static IntRef from(@NotNull Field field, @Nullable Object target) {
        return new FieldIntRef(field, target);
    }

    @NotNull
    static IntRef from(@NotNull IntSupplier getter, @NotNull IntConsumer setter) {
        return new CustomIntRef(getter, setter);
    }

}
