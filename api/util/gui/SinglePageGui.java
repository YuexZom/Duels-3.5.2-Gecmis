/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.gui;

import me.realized.duels.util.StringUtil;
import me.realized.duels.util.gui.AbstractGui;
import me.realized.duels.util.gui.Button;
import me.realized.duels.util.inventory.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class SinglePageGui<P extends JavaPlugin>
extends AbstractGui<P> {
    protected final Inventory inventory;

    public SinglePageGui(P plugin, String title, int rows) {
        super(plugin);
        this.inventory = InventoryBuilder.of(StringUtil.color(title), rows * 9).build();
    }

    protected void set(int slot, Button<P> button) {
        this.set(this.inventory, slot, button);
    }

    protected void set(int from, int to, int height, Button<P> button) {
        this.set(this.inventory, from, to, height, button);
    }

    public void update(Player player, Button<P> button) {
        this.update(player, this.inventory, button);
    }

    @Override
    public void open(Player ... players) {
        for (Player player : players) {
            this.update(player);
            player.openInventory(this.inventory);
        }
    }

    @Override
    public boolean isPart(Inventory inventory) {
        return inventory.equals(this.inventory);
    }

    @Override
    public void on(Player player, Inventory top, InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) {
            return;
        }
        event.setCancelled(true);
        if (!clicked.equals(top)) {
            return;
        }
        Button button = this.get(this.inventory, event.getSlot());
        if (button == null) {
            return;
        }
        button.onClick(player);
    }
}

