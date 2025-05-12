/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.potion.PotionType
 */
package me.realized.duels.util.compat;

import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.compat.Panes;
import me.realized.duels.util.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public final class Items {
    private static final String PANE = "STAINED_GLASS_PANE";
    public static final ItemStack ORANGE_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)1) : ItemBuilder.of(Material.ORANGE_STAINED_GLASS_PANE)).name(" ").build();
    public static final ItemStack BLUE_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)11) : ItemBuilder.of(Material.BLUE_STAINED_GLASS_PANE)).name(" ").build();
    public static final ItemStack RED_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)14) : ItemBuilder.of(Material.RED_STAINED_GLASS_PANE)).build();
    public static final ItemStack GRAY_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)7) : ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)).name(" ").build();
    public static final ItemStack WHITE_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)0) : ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE)).name(" ").build();
    public static final ItemStack GREEN_PANE = (CompatUtil.isPre1_13() ? ItemBuilder.of("STAINED_GLASS_PANE", 1, (short)5) : ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE)).build();
    public static final ItemStack HEAD = (CompatUtil.isPre1_13() ? ItemBuilder.of("SKULL_ITEM", 1, (short)3) : ItemBuilder.of(Material.PLAYER_HEAD)).build();
    public static final Material SKELETON_HEAD = CompatUtil.isPre1_13() ? Material.matchMaterial((String)"SKULL_ITEM") : Material.SKELETON_SKULL;
    public static final ItemStack OFF = (CompatUtil.isPre1_13() ? ItemBuilder.of("INK_SACK", 1, (short)8) : ItemBuilder.of(Material.GRAY_DYE)).build();
    public static final ItemStack ON = (CompatUtil.isPre1_13() ? ItemBuilder.of("INK_SACK", 1, (short)10) : ItemBuilder.of(Material.LIME_DYE)).build();
    public static final Material MUSHROOM_SOUP = CompatUtil.isPre1_13() ? Material.matchMaterial((String)"MUSHROOM_SOUP") : Material.MUSHROOM_STEW;
    public static final Material EMPTY_MAP = CompatUtil.isPre1_13() ? Material.matchMaterial((String)"EMPTY_MAP") : Material.MAP;
    public static final Material SIGN = CompatUtil.isPre1_14() ? Material.matchMaterial((String)"SIGN") : Material.OAK_SIGN;
    public static final ItemStack HEAL_SPLASH_POTION = (CompatUtil.isPre1_9() ? ItemBuilder.of(Material.POTION, 1, (short)16421) : ItemBuilder.of(Material.SPLASH_POTION).potion(PotionType.INSTANT_HEAL, false, true)).build();
    public static final ItemStack WATER_BREATHING_POTION = (CompatUtil.isPre1_9() ? ItemBuilder.of(Material.POTION, 1, (short)8237) : ItemBuilder.of(Material.POTION).potion(PotionType.WATER_BREATHING, false, false)).build();
    public static final ItemStack ENCHANTED_GOLDEN_APPLE = CompatUtil.isPre1_13() ? ItemBuilder.of(Material.GOLDEN_APPLE, 1, (short)1).build() : ItemBuilder.of(Material.ENCHANTED_GOLDEN_APPLE).build();

    public static boolean equals(ItemStack item, ItemStack other) {
        return item.getType() == other.getType() && Items.getDurability(item) == Items.getDurability(other);
    }

    public static ItemStack from(String type, short data) {
        if (type.equalsIgnoreCase(PANE) && !CompatUtil.isPre1_13()) {
            return ItemBuilder.of(Panes.from(data)).name(" ").build();
        }
        return ItemBuilder.of(type, 1, data).name(" ").build();
    }

    public static short getDurability(ItemStack item) {
        if (CompatUtil.isPre1_13()) {
            return item.getDurability();
        }
        ItemMeta meta = item.getItemMeta();
        return meta == null ? (short)0 : (short)((Damageable)meta).getDamage();
    }

    public static void setDurability(ItemStack item, short durability) {
        if (CompatUtil.isPre1_13()) {
            item.setDurability(durability);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ((Damageable)meta).setDamage((int)durability);
            item.setItemMeta(meta);
        }
    }

    public static boolean isHealSplash(ItemStack item) {
        if (CompatUtil.isPre1_9()) {
            return Items.equals(HEAL_SPLASH_POTION, item);
        }
        if (item.getType() != Material.SPLASH_POTION) {
            return false;
        }
        PotionMeta meta = (PotionMeta)item.getItemMeta();
        return meta != null && meta.getBasePotionData().getType() == PotionType.INSTANT_HEAL;
    }

    private Items() {
    }
}

