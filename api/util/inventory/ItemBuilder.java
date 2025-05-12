/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.potion.PotionData
 *  org.bukkit.potion.PotionType
 */
package me.realized.duels.util.inventory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import me.realized.duels.util.EnumUtil;
import me.realized.duels.util.StringUtil;
import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.compat.Items;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public final class ItemBuilder {
    private final ItemStack result;

    private ItemBuilder(Material type, int amount, short durability) {
        this.result = new ItemStack(type, amount);
        Items.setDurability(this.result, durability);
    }

    private ItemBuilder(String type, int amount, short durability) {
        this(Material.matchMaterial((String)type), amount, durability);
    }

    private ItemBuilder(ItemStack item) {
        this.result = item;
    }

    public static ItemBuilder of(Material type) {
        return ItemBuilder.of(type, 1);
    }

    public static ItemBuilder of(Material type, int amount) {
        return ItemBuilder.of(type, amount, (short)0);
    }

    public static ItemBuilder of(Material type, int amount, short durability) {
        return new ItemBuilder(type, amount, durability);
    }

    public static ItemBuilder of(String type, int amount, short durability) {
        return new ItemBuilder(type, amount, durability);
    }

    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }

    public ItemBuilder editMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = this.result.getItemMeta();
        consumer.accept(meta);
        this.result.setItemMeta(meta);
        return this;
    }

    public ItemBuilder name(String name) {
        return this.editMeta(meta -> meta.setDisplayName(StringUtil.color(name)));
    }

    public ItemBuilder lore(List<String> lore) {
        return this.editMeta(meta -> meta.setLore(StringUtil.color(lore)));
    }

    public ItemBuilder lore(String ... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.result.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder unbreakable() {
        return this.editMeta(meta -> {
            if (CompatUtil.isPre1_12()) {
                meta.spigot().setUnbreakable(true);
            } else {
                meta.setUnbreakable(true);
            }
        });
    }

    public ItemBuilder head(String owner) {
        return this.editMeta(meta -> {
            if (owner != null && Items.equals(Items.HEAD, this.result)) {
                SkullMeta skullMeta = (SkullMeta)meta;
                skullMeta.setOwner(owner);
            }
        });
    }

    public ItemBuilder leatherArmorColor(String color) {
        return this.editMeta(meta -> {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)meta;
            if (color != null) {
                leatherArmorMeta.setColor(DyeColor.valueOf((String)color).getColor());
            }
        });
    }

    public ItemBuilder potion(PotionType type, boolean extended, boolean upgraded) {
        PotionMeta meta = (PotionMeta)this.result.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        this.result.setItemMeta((ItemMeta)meta);
        return this;
    }

    public ItemBuilder attribute(String name, int operation, double amount, String slotName) {
        return this.editMeta(meta -> {
            AttributeModifier modifier;
            Attribute attribute = EnumUtil.getByName(this.attributeNameToEnum(name), Attribute.class);
            if (attribute == null) {
                return;
            }
            if (slotName != null) {
                EquipmentSlot slot = EnumUtil.getByName(slotName, EquipmentSlot.class);
                if (slot == null) {
                    return;
                }
                modifier = new AttributeModifier(UUID.randomUUID(), name, amount, AttributeModifier.Operation.values()[operation], slot);
            } else {
                modifier = new AttributeModifier(UUID.randomUUID(), name, amount, AttributeModifier.Operation.values()[operation]);
            }
            meta.addAttributeModifier(attribute, modifier);
        });
    }

    private String attributeNameToEnum(String name) {
        int len = name.length();
        int capitalLetterIndex = -1;
        for (int i = 0; i < len; ++i) {
            if (!Character.isUpperCase(name.charAt(i))) continue;
            capitalLetterIndex = i;
            break;
        }
        if (capitalLetterIndex != -1) {
            name = name.substring(0, capitalLetterIndex) + "_" + name.substring(capitalLetterIndex);
        }
        return name.replace(".", "_").toUpperCase();
    }

    public ItemStack build() {
        return this.result;
    }
}

