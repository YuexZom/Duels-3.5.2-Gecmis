/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeInstance
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package me.realized.duels.util;

import me.realized.duels.util.compat.CompatUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class PlayerUtil {
    private static final double DEFAULT_MAX_HEALTH = 20.0;
    private static final int DEFAULT_MAX_FOOD_LEVEL = 20;

    public static double getMaxHealth(Player player) {
        if (CompatUtil.isPre1_9()) {
            return player.getMaxHealth();
        }
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            return 20.0;
        }
        return attribute.getValue();
    }

    private static void setMaxHealth(Player player) {
        player.setHealth(PlayerUtil.getMaxHealth(player));
    }

    public static void reset(Player player) {
        player.setFireTicks(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        PlayerUtil.setMaxHealth(player);
        player.setFoodLevel(20);
        player.setItemOnCursor(null);
        Inventory top = player.getOpenInventory().getTopInventory();
        if (top != null && top.getType() == InventoryType.CRAFTING) {
            top.clear();
        }
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().clear();
        player.updateInventory();
    }

    private PlayerUtil() {
    }
}

