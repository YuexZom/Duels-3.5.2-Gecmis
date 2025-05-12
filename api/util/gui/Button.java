/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.gui;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import me.realized.duels.util.StringUtil;
import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.compat.Items;
import me.realized.duels.util.compat.Skulls;
import me.realized.duels.util.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Button<P extends JavaPlugin> {
    protected final P plugin;
    private ItemStack displayed;

    public Button(P plugin, ItemStack displayed) {
        this.plugin = plugin;
        this.displayed = displayed;
    }

    protected void editMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = this.getDisplayed().getItemMeta();
        consumer.accept(meta);
        this.getDisplayed().setItemMeta(meta);
    }

    protected void setDisplayName(String name) {
        this.editMeta(meta -> meta.setDisplayName(StringUtil.color(name)));
    }

    protected void setLore(List<String> lore) {
        this.editMeta(meta -> meta.setLore(StringUtil.color(lore)));
    }

    protected void setLore(String ... lore) {
        this.setLore(Arrays.asList(lore));
    }

    protected void setOwner(Player player) {
        if (Items.equals(this.displayed, Items.HEAD)) {
            this.editMeta(meta -> Skulls.setProfile((SkullMeta)meta, player));
        }
    }

    protected void setGlow(boolean glow) {
        if (this.displayed.getType().name().endsWith("GOLDEN_APPLE")) {
            ItemStack item = glow ? Items.ENCHANTED_GOLDEN_APPLE.clone() : ItemBuilder.of(Material.GOLDEN_APPLE).build();
            item.setItemMeta(this.getDisplayed().getItemMeta());
            this.setDisplayed(item);
            return;
        }
        this.editMeta(meta -> {
            if (glow) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                if (CompatUtil.hasItemFlag()) {
                    meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
            } else {
                meta.removeEnchant(Enchantment.DURABILITY);
                if (CompatUtil.hasItemFlag()) {
                    meta.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
            }
        });
    }

    public void update(Player player) {
    }

    public void onClick(Player player) {
    }

    public ItemStack getDisplayed() {
        return this.displayed;
    }

    public void setDisplayed(ItemStack displayed) {
        this.displayed = displayed;
    }
}

