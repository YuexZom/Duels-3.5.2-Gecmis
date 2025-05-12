/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.collection;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {
    public static <T> Stream<T> asStream(Iterable<T> iterable) {
        return StreamUtil.asStream(iterable.spliterator());
    }

    public static <T> Stream<T> asStream(Iterator<T> iterator) {
        return StreamUtil.asStream(Spliterators.spliteratorUnknownSize(iterator, 0));
    }

    public static <T> Stream<T> asStream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }
}

