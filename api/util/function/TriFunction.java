/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<S, T, U, R> {
    public R apply(S var1, T var2, U var3);

    default public <V> TriFunction<S, T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (s, t, u) -> after.apply((R)this.apply(s, t, u));
    }
}

