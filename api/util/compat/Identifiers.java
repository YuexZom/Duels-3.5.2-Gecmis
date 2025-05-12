/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.NamespacedKey
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.realized.duels.util.compat;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.compat.nbt.NBT;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public final class Identifiers {
    private static final transient String DUELS_ITEM_IDENTIFIER = "DuelsKitContent";

    public static ItemStack addIdentifier(ItemStack item) {
        if (CompatUtil.isPre1_14()) {
            return NBT.setItemString(item, DUELS_ITEM_IDENTIFIER, true);
        }
        NamespacedKey key = new NamespacedKey((Plugin)DuelsPlugin.getInstance(), DUELS_ITEM_IDENTIFIER);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (Object)1);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean hasIdentifier(ItemStack item) {
        if (CompatUtil.isPre1_14()) {
            return NBT.hasItemKey(item, DUELS_ITEM_IDENTIFIER);
        }
        NamespacedKey key = new NamespacedKey((Plugin)DuelsPlugin.getInstance(), DUELS_ITEM_IDENTIFIER);
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    public static ItemStack removeIdentifier(ItemStack item) {
        if (CompatUtil.isPre1_14()) {
            return NBT.removeItemTag(item, DUELS_ITEM_IDENTIFIER);
        }
        NamespacedKey key = new NamespacedKey((Plugin)DuelsPlugin.getInstance(), DUELS_ITEM_IDENTIFIER);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
        return item;
    }

    private Identifiers() {
    }
}

