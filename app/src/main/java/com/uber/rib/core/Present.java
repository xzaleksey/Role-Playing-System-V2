//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.uber.rib.core;

import java.util.function.Function;

final class Present<T> extends Optional<T> {
    private final T reference;

    Present(T reference) {
        this.reference = reference;
    }

    public boolean isPresent() {
        return true;
    }

    public T get() {
        return this.reference;
    }

    public T or(T defaultValue) {
        Preconditions.checkNotNull(defaultValue);
        return this.reference;
    }

    public Optional<T> or(Optional<? extends T> secondChoice) {
        Preconditions.checkNotNull(secondChoice);
        return this;
    }

    public T orNull() {
        return this.reference;
    }

    public <V> Optional<V> transform(Function<? super T, V> function) {
        return new Present(Preconditions.checkNotNull(function.apply(this.reference)));
    }

    public boolean equals(Object object) {
        if (object instanceof Present) {
            Present<?> other = (Present)object;
            return this.reference.equals(other.reference);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }

    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
}
