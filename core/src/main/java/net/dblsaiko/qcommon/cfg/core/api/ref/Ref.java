package net.dblsaiko.qcommon.cfg.core.api.ref;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.ref.CustomRef;
import net.dblsaiko.qcommon.cfg.core.api.impl.ref.FieldRef;

/**
 * A mutable reference to an object.
 */
public interface Ref<T> {

    /**
     * Get the value behind this reference.
     *
     * @return the value
     */
    T get();

    /**
     * Set the value behind this reference
     *
     * @param value the new value
     */
    void set(T value);

    /**
     * Create a {@link Ref} from a {@link Field}.
     *
     * @param field  the field
     * @param target the instance of this field, or null if it is a static field
     * @return the new {@link Ref}
     */
    @NotNull
    static <T> Ref<T> from(@NotNull Field field, @Nullable Object target) {
        return new FieldRef<>(field, target);
    }

    /**
     * Create a {@link Ref} from a getter and setter method.
     *
     * @param getter the getter
     * @param setter the setter
     * @return the new {@link Ref}
     */
    @NotNull
    static <T> Ref<T> from(@NotNull Supplier<T> getter, @NotNull Consumer<T> setter) {
        return new CustomRef<>(getter, setter);
    }

}
