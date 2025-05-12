/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.Inventory
 */
package me.realized.duels.util.compat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.inventory.Inventory;

public final class Inventories {
    private static final Field CB_INVENTORY = ReflectionUtil.getDeclaredField(ReflectionUtil.getCBClass("inventory.CraftInventory"), "inventory");
    private static final Field CB_INVENTORY_TITLE = ReflectionUtil.getDeclaredField(ReflectionUtil.getCBClass("inventory.CraftInventoryCustom$MinecraftInventory"), "title");
    private static final Method CHAT_SERIALIZER_A = CompatUtil.is1_13() ? ReflectionUtil.getMethod(ReflectionUtil.getNMSClass("IChatBaseComponent$ChatSerializer"), "a", String.class) : null;

    public static void setTitle(Inventory inventory, String title) {
        try {
            Object value = title;
            if (CHAT_SERIALIZER_A != null) {
                value = CHAT_SERIALIZER_A.invoke(null, "{\"text\": \"" + title + "\"}");
            }
            CB_INVENTORY_TITLE.set(CB_INVENTORY.get(inventory), value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private Inventories() {
    }
}

