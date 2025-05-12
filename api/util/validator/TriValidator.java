/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.validator;

public interface TriValidator<T1, T2, T3> {
    public boolean shouldValidate();

    public boolean validate(T1 var1, T2 var2, T3 var3);
}

