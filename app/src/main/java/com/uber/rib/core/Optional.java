//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.uber.rib.core;

import androidx.annotation.Nullable;

import java.util.function.Function;

public abstract class Optional<T> {
    Optional() {
    }

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(@Nullable T nullableReference) {
        return (Optional)(nullableReference == null ? absent() : new Present(nullableReference));
    }

    public abstract boolean isPresent();

    public abstract T get();

    public abstract T or(T var1);

    public abstract Optional<T> or(Optional<? extends T> var1);

    @Nullable
    public abstract T orNull();

    public abstract <V> Optional<V> transform(Function<? super T, V> var1);

    public abstract boolean equals(Object var1);

    public abstract int hashCode();

    public abstract String toString();
}
