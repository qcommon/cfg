package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.ref.CustomFloatRef;
import net.dblsaiko.qcommon.cfg.core.api.impl.ref.FieldFloatRef;

public interface FloatRef {

    float get();

    void set(float value);

    @NotNull
    static FloatRef from(@NotNull Field field, @Nullable Object target) {
        return new FieldFloatRef(field, target);
    }

    @NotNull
    static FloatRef from(@NotNull Supplier<Float> getter, @NotNull Consumer<Float> setter) {
        return new CustomFloatRef(getter, setter);
    }

}
