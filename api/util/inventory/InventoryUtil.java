/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package me.realized.duels.util.inventory;

import com.google.common.collect.ObjectArrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class InventoryUtil {
    private static final String INVENTORY_IDENTIFIER = "INVENTORY";
    private static final String ARMOR_IDENTIFIER = "ARMOR";

    public static void addToMap(PlayerInventory inventory, Map<String, Map<Integer, ItemStack>> items) {
        HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            contents.put(i, item.clone());
        }
        items.put(INVENTORY_IDENTIFIER, contents);
        HashMap<Integer, ItemStack> armorContents = new HashMap<Integer, ItemStack>();
        for (int i = inventory.getArmorContents().length - 1; i >= 0; --i) {
            ItemStack item = inventory.getArmorContents()[i];
            if (item == null || item.getType() == Material.AIR) continue;
            armorContents.put(4 - i, inventory.getArmorContents()[i].clone());
        }
        items.put(ARMOR_IDENTIFIER, armorContents);
    }

    public static void fillFromMap(PlayerInventory inventory, Map<String, Map<Integer, ItemStack>> items) {
        Map<Integer, ItemStack> armorItems;
        Map<Integer, ItemStack> inventoryItems = items.get(INVENTORY_IDENTIFIER);
        if (inventoryItems != null) {
            for (Map.Entry<Integer, ItemStack> entry : inventoryItems.entrySet()) {
                inventory.setItem(entry.getKey().intValue(), entry.getValue().clone());
            }
        }
        if ((armorItems = items.get(ARMOR_IDENTIFIER)) != null) {
            ItemStack[] armor = new ItemStack[4];
            armorItems.forEach((slot, item) -> {
                itemStackArray[4 - slot.intValue()] = item.clone();
            });
            inventory.setArmorContents(armor);
        }
    }

    public static boolean hasItem(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack item : ObjectArrays.concat(inventory.getArmorContents(), inventory.getContents(), ItemStack.class)) {
            if (item == null || item.getType() == Material.AIR) continue;
            return true;
        }
        return false;
    }

    public static boolean addOrDrop(Player player, Collection<ItemStack> items) {
        if (items.isEmpty()) {
            return false;
        }
        HashMap result = player.getInventory().addItem((ItemStack[])items.stream().filter(Objects::nonNull).toArray(ItemStack[]::new));
        if (!result.isEmpty()) {
            result.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        }
        return true;
    }

    public static ItemStack getItemInHand(Player player) {
        return player.getInventory().getItem(player.getInventory().getHeldItemSlot());
    }

    private InventoryUtil() {
    }
}

