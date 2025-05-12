/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util.inventory;

import java.util.function.Consumer;

public final class Slots {
    private Slots() {
    }

    public static void run(int from, int to, int height, Consumer<Integer> action) {
        for (int h = 0; h < height; ++h) {
            for (int slot = from; slot < to; ++slot) {
                action.accept(slot + h * 9);
            }
        }
    }

    public static void run(int from, int to, Consumer<Integer> action) {
        for (int slot = from; slot < to; ++slot) {
            action.accept(slot);
        }
    }
}

