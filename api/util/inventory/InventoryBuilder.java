/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package me.realized.duels.util.inventory;

import me.realized.duels.util.inventory.Slots;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryBuilder {
    private final Inventory inventory;

    private InventoryBuilder(String title, int size) {
        this.inventory = Bukkit.createInventory(null, (int)size, (String)title);
    }

    public static InventoryBuilder of(String title, int size) {
        return new InventoryBuilder(title, size);
    }

    public InventoryBuilder set(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder fillRange(int from, int to, ItemStack item) {
        Slots.run(from, to, slot -> this.inventory.setItem(slot.intValue(), item));
        return this;
    }

    public Inventory build() {
        return this.inventory;
    }
}

