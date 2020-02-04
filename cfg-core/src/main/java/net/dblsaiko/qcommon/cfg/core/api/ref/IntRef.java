package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import net.dblsaiko.qcommon.cfg.core.ref.CustomIntRef;
import net.dblsaiko.qcommon.cfg.core.ref.FieldIntRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A mutable reference to an int.
 */
public interface IntRef {

    /**
     * Get the value behind this reference.
     *
     * @return the value
     */
    int get();

    /**
     * Set the value behind this reference
     *
     * @param value the new value
     */
    void set(int value);

    /**
     * Create a {@link IntRef} from a {@link Field}.
     *
     * @param field  the field
     * @param target the instance of this field, or null if it is a static field
     * @return the new {@link IntRef}
     */
    @NotNull
    static IntRef from(@NotNull Field field, @Nullable Object target) {
        return new FieldIntRef(field, target);
    }

    /**
     * Create a {@link IntRef} from a getter and setter method.
     *
     * @param getter the getter
     * @param setter the setter
     * @return the new {@link IntRef}
     */
    @NotNull
    static IntRef from(@NotNull IntSupplier getter, @NotNull IntConsumer setter) {
        return new CustomIntRef(getter, setter);
    }

}
