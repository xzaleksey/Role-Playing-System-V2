//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.uber.rib.core;

import android.support.annotation.Nullable;
import java.util.function.Function;

final class Absent<T> extends Optional<T> {
    static final Absent<Object> INSTANCE = new Absent<>();

    private Absent() {
    }

    @SuppressWarnings("unchecked")
    static <T> Optional<T> withType() {
        return (Optional<T>) INSTANCE;
    }

    public boolean isPresent() {
        return false;
    }

    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    public T or(T defaultValue) {
        return Preconditions.checkNotNull(defaultValue);
    }

    @SuppressWarnings("unchecked")
    public Optional<T> or(Optional<? extends T> secondChoice) {
        return (Optional)Preconditions.checkNotNull(secondChoice);
    }

    @Nullable
    public T orNull() {
        return null;
    }

    public <V> Optional<V> transform(Function<? super T, V> function) {
        Preconditions.checkNotNull(function);
        return Optional.absent();
    }

    public boolean equals(Object object) {
        return object == this;
    }

    public int hashCode() {
        return 1502476572;
    }

    public String toString() {
        return "Optional.absent()";
    }
}
