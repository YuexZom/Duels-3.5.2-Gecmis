/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.config.convert;

import java.util.HashMap;
import java.util.Map;
import me.realized.duels.util.config.convert.Converter;

public final class ConverterUtil {
    public static Converter merge(Converter ... converters) {
        final HashMap<String, String> keys = new HashMap<String, String>();
        for (Converter converter : converters) {
            keys.putAll(converter.renamedKeys());
        }
        return new Converter(){

            @Override
            public Map<String, String> renamedKeys() {
                return keys;
            }
        };
    }

    private ConverterUtil() {
    }
}

