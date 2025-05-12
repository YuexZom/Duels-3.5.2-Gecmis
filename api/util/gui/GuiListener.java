/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.gui;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.realized.duels.util.Loadable;
import me.realized.duels.util.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GuiListener<P extends JavaPlugin>
implements Loadable,
Listener {
    private final Multimap<UUID, AbstractGui<P>> privateGuis = HashMultimap.create();
    private final List<AbstractGui<P>> publicGuis = new ArrayList<AbstractGui<P>>();

    public GuiListener(P plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    @Override
    public void handleLoad() {
    }

    @Override
    public void handleUnload() {
        this.privateGuis.values().forEach(AbstractGui::clear);
        this.privateGuis.clear();
        this.publicGuis.forEach(AbstractGui::clear);
        this.publicGuis.clear();
    }

    public void addGui(AbstractGui<P> gui) {
        this.publicGuis.add(gui);
    }

    public <T extends AbstractGui<P>> T addGui(Player player, T gui, boolean removeSameType) {
        Collection<AbstractGui<P>> guis;
        if (removeSameType && (guis = this.privateGuis.asMap().get(player.getUniqueId())) != null) {
            guis.removeIf(cached -> gui.getClass().isInstance(cached));
        }
        this.privateGuis.put(player.getUniqueId(), gui);
        return gui;
    }

    public <T extends AbstractGui<P>> T addGui(Player player, T gui) {
        return this.addGui(player, gui, false);
    }

    public void removeGui(AbstractGui<P> gui) {
        gui.clear();
        this.publicGuis.remove(gui);
    }

    public void removeGui(Player player, AbstractGui<P> gui) {
        gui.clear();
        Collection<AbstractGui<P>> guis = this.privateGuis.asMap().get(player.getUniqueId());
        if (guis != null) {
            guis.remove(gui);
        }
    }

    private List<AbstractGui<P>> get(Player player) {
        ArrayList<AbstractGui<P>> guis = Lists.newArrayList(this.publicGuis);
        if (this.privateGuis.containsKey(player.getUniqueId())) {
            guis.addAll(this.privateGuis.get(player.getUniqueId()));
        }
        return guis;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory top = player.getOpenInventory().getTopInventory();
        for (AbstractGui<P> gui : this.get(player)) {
            if (!gui.isPart(top)) continue;
            gui.on(player, top, event);
            break;
        }
    }

    @EventHandler
    public void on(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = event.getInventory();
        for (AbstractGui<P> gui : this.get(player)) {
            if (!gui.isPart(inventory)) continue;
            gui.on(player, event.getRawSlots(), event);
            break;
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Inventory inventory = event.getInventory();
        for (AbstractGui<P> gui : this.get(player)) {
            if (!gui.isPart(inventory)) continue;
            gui.on(player, event.getInventory(), event);
            break;
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        this.privateGuis.removeAll(event.getPlayer().getUniqueId());
    }
}

