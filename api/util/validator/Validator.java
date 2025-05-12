/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.validator;

public interface Validator<T> {
    public boolean shouldValidate();

    public boolean validate(T var1);
}

