package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.ref.CustomFloatRef;
import net.dblsaiko.qcommon.cfg.core.api.impl.ref.FieldFloatRef;

/**
 * A mutable reference to a float.
 */
public interface FloatRef {

    /**
     * Get the value behind this reference.
     *
     * @return the value
     */
    float get();

    /**
     * Set the value behind this reference
     *
     * @param value the new value
     */
    void set(float value);

    /**
     * Create a {@link FloatRef} from a {@link Field}.
     *
     * @param field  the field
     * @param target the instance of this field, or null if it is a static field
     * @return the new {@link FloatRef}
     */
    @NotNull
    static FloatRef from(@NotNull Field field, @Nullable Object target) {
        return new FieldFloatRef(field, target);
    }

    /**
     * Create a {@link FloatRef} from a getter and setter method.
     *
     * @param getter the getter
     * @param setter the setter
     * @return the new {@link FloatRef}
     */
    @NotNull
    static FloatRef from(@NotNull Supplier<Float> getter, @NotNull Consumer<Float> setter) {
        return new CustomFloatRef(getter, setter);
    }

}
