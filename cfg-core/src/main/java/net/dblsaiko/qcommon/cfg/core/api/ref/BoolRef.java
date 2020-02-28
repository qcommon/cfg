package net.dblsaiko.qcommon.cfg.core.api.ref;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.dblsaiko.qcommon.cfg.core.ref.CustomBoolRef;
import net.dblsaiko.qcommon.cfg.core.ref.FieldBoolRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.BooleanSupplier;

/**
 * A mutable reference to a boolean.
 */
public interface BoolRef {

    /**
     * Get the value behind this reference.
     *
     * @return the value
     */
    boolean get();

    /**
     * Set the value behind this reference
     *
     * @param value the new value
     */
    void set(boolean value);

    /**
     * Create a {@link BoolRef} from a {@link Field}.
     *
     * @param field  the field
     * @param target the instance of this field, or null if it is a static field
     * @return the new {@link BoolRef}
     */
    @NotNull
    static BoolRef from(@NotNull Field field, @Nullable Object target) {
        return new FieldBoolRef(field, target);
    }

    /**
     * Create a {@link BoolRef} from a getter and setter method.
     *
     * @param getter the getter
     * @param setter the setter
     * @return the new {@link BoolRef}
     */
    @NotNull
    static BoolRef from(@NotNull BooleanSupplier getter, @NotNull BooleanConsumer setter) {
        return new CustomBoolRef(getter, setter);
    }

}
