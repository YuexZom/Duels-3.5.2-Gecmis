/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.realized.duels.util.gui.Button;
import me.realized.duels.util.inventory.Slots;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractGui<P extends JavaPlugin> {
    protected final P plugin;
    private final long creation;
    private final Map<Inventory, Map<Integer, Button<P>>> buttons = new HashMap<Inventory, Map<Integer, Button<P>>>();

    public AbstractGui(P plugin) {
        this.plugin = plugin;
        this.creation = System.currentTimeMillis();
    }

    public abstract void open(Player ... var1);

    public abstract boolean isPart(Inventory var1);

    public abstract void on(Player var1, Inventory var2, InventoryClickEvent var3);

    public void on(Player player, Inventory inventory, InventoryCloseEvent event) {
    }

    public void on(Player player, Set<Integer> rawSlots, InventoryDragEvent event) {
        event.setCancelled(true);
    }

    public Button<P> get(Inventory inventory, int slot) {
        Map<Integer, Button<P>> buttons = this.buttons.get(inventory);
        return buttons != null ? buttons.get(slot) : null;
    }

    public void set(Inventory inventory, int slot, Button<P> button) {
        this.buttons.computeIfAbsent(inventory, result -> new HashMap()).put(slot, button);
        inventory.setItem(slot, button.getDisplayed());
    }

    public void set(Inventory inventory, int from, int to, int height, Button<P> button) {
        Slots.run(from, to, height, slot -> this.set(inventory, (int)slot, button));
    }

    public void set(Inventory inventory, int from, int to, Button<P> button) {
        Slots.run(from, to, slot -> this.set(inventory, (int)slot, button));
    }

    public void remove(Inventory inventory) {
        this.buttons.remove(inventory);
    }

    public Button<P> remove(Inventory inventory, int slot) {
        Map<Integer, Button<P>> buttons = this.buttons.get(inventory);
        return buttons != null ? buttons.remove(slot) : null;
    }

    public void update(Player player, Inventory inventory, Button<P> button) {
        Map<Integer, Button<P>> cached = this.buttons.get(inventory);
        if (cached == null) {
            return;
        }
        button.update(player);
        cached.entrySet().stream().filter(entry -> ((Button)entry.getValue()).equals(button)).findFirst().ifPresent(entry -> inventory.setItem(((Integer)entry.getKey()).intValue(), button.getDisplayed()));
    }

    public void update(Player player) {
        this.buttons.forEach((inventory, data) -> data.forEach((slot, button) -> {
            button.update(player);
            inventory.setItem(slot.intValue(), button.getDisplayed());
        }));
    }

    public void clear() {
        this.buttons.keySet().forEach(Inventory::clear);
    }

    public long getCreation() {
        return this.creation;
    }
}

