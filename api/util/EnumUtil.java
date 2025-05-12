/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util;

import java.util.Arrays;

public final class EnumUtil {
    private EnumUtil() {
    }

    public static <E extends Enum<E>> E getByName(String name, Class<E> clazz) {
        return (E)((Enum)clazz.cast(Arrays.stream(clazz.getEnumConstants()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null)));
    }
}

