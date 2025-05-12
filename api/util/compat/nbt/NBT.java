/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package me.realized.duels.util.compat.nbt;

import java.lang.reflect.Method;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

public final class NBT {
    private static final Method AS_NMS_COPY;
    private static final Method AS_BUKKIT_COPY;
    private static final Class<?> TAG_COMPOUND;
    private static final Method GET_TAG;
    private static final Method SET_TAG;
    private static final Method SET_STRING;
    private static final Method REMOVE;
    private static final Method HAS_KEY;

    public static ItemStack setItemString(ItemStack item, String key, Object value) {
        try {
            Object nmsItem = AS_NMS_COPY.invoke(null, item);
            Object tag = GET_TAG.invoke(nmsItem, new Object[0]);
            if (tag == null) {
                tag = TAG_COMPOUND.newInstance();
            }
            SET_STRING.invoke(tag, key, value.toString());
            SET_TAG.invoke(nmsItem, tag);
            return (ItemStack)AS_BUKKIT_COPY.invoke(null, nmsItem);
        } catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }

    public static ItemStack removeItemTag(ItemStack item, String key) {
        try {
            Object nmsItem = AS_NMS_COPY.invoke(null, item);
            Object tag = GET_TAG.invoke(nmsItem, new Object[0]);
            if (tag == null) {
                return item;
            }
            REMOVE.invoke(tag, key);
            SET_TAG.invoke(nmsItem, tag);
            return (ItemStack)AS_BUKKIT_COPY.invoke(null, nmsItem);
        } catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }

    public static boolean hasItemKey(ItemStack item, String key) {
        try {
            Object nmsItem = AS_NMS_COPY.invoke(null, item);
            if (nmsItem == null) {
                return false;
            }
            Object tag = GET_TAG.invoke(nmsItem, new Object[0]);
            return tag != null && (Boolean)HAS_KEY.invoke(tag, key) != false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private NBT() {
    }

    static {
        Class<?> CB_ITEMSTACK = ReflectionUtil.getCBClass("inventory.CraftItemStack");
        Class<?> NMS_ITEMSTACK = ReflectionUtil.getNMSClass("ItemStack");
        AS_NMS_COPY = ReflectionUtil.getMethod(CB_ITEMSTACK, "asNMSCopy", ItemStack.class);
        AS_BUKKIT_COPY = ReflectionUtil.getMethod(CB_ITEMSTACK, "asBukkitCopy", NMS_ITEMSTACK);
        TAG_COMPOUND = ReflectionUtil.getNMSClass("NBTTagCompound");
        GET_TAG = ReflectionUtil.getMethod(NMS_ITEMSTACK, "getTag", new Class[0]);
        SET_TAG = ReflectionUtil.getMethod(NMS_ITEMSTACK, "setTag", TAG_COMPOUND);
        SET_STRING = ReflectionUtil.getMethod(TAG_COMPOUND, "setString", String.class, String.class);
        REMOVE = ReflectionUtil.getMethod(TAG_COMPOUND, "remove", String.class);
        HAS_KEY = ReflectionUtil.getMethod(TAG_COMPOUND, "hasKey", String.class);
    }
}

