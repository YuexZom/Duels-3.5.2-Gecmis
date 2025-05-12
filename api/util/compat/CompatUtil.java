/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockCanBuildEvent
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.realized.duels.util.compat;

import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public final class CompatUtil {
    private static final boolean ATTRIBUTES = ReflectionUtil.getMethodUnsafe(ItemMeta.class, "getAttributeModifiers", new Class[0]) != null;
    private static final boolean ITEM_FLAGS = ReflectionUtil.getClassUnsafe("org.bukkit.inventory.ItemFlag") != null;
    private static final boolean SEND_TITLE = ReflectionUtil.getMethodUnsafe(Player.class, "sendTitle", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE) != null;
    private static final boolean HIDE_PLAYER = ReflectionUtil.getMethodUnsafe(Player.class, "hidePlayer", Plugin.class, Player.class) != null;
    private static final boolean SET_COLLIDABLE = ReflectionUtil.getMethodUnsafe(LivingEntity.class, "setCollidable", Boolean.TYPE) != null;
    private static final boolean GET_PLAYER = ReflectionUtil.getMethodUnsafe(BlockCanBuildEvent.class, "getPlayer", new Class[0]) != null;

    public static boolean is1_13() {
        return ReflectionUtil.getMajorVersion() == 13;
    }

    public static boolean isPre1_14() {
        return ReflectionUtil.getMajorVersion() < 14;
    }

    public static boolean isPre1_13() {
        return ReflectionUtil.getMajorVersion() < 13;
    }

    public static boolean isPre1_12() {
        return ReflectionUtil.getMajorVersion() < 12;
    }

    public static boolean isPre1_10() {
        return ReflectionUtil.getMajorVersion() < 10;
    }

    public static boolean isPre1_9() {
        return ReflectionUtil.getMajorVersion() < 9;
    }

    public static boolean hasAttributes() {
        return ATTRIBUTES;
    }

    public static boolean hasItemFlag() {
        return ITEM_FLAGS;
    }

    public static boolean hasSendTitle() {
        return SEND_TITLE;
    }

    public static boolean hasHidePlayer() {
        return HIDE_PLAYER;
    }

    public static boolean hasSetCollidable() {
        return SET_COLLIDABLE;
    }

    public static boolean hasGetPlayer() {
        return GET_PLAYER;
    }

    private CompatUtil() {
    }
}

