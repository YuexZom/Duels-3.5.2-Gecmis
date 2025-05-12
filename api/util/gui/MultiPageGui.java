/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.gui;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.function.Consumer;
import me.realized.duels.util.compat.Inventories;
import me.realized.duels.util.compat.Items;
import me.realized.duels.util.gui.AbstractGui;
import me.realized.duels.util.gui.Button;
import me.realized.duels.util.inventory.InventoryBuilder;
import me.realized.duels.util.inventory.Slots;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiPageGui<P extends JavaPlugin>
extends AbstractGui<P> {
    private final String title;
    private final int size;
    private final int prevPageSlot;
    private final int nextPageSlot;
    private final Collection<? extends Button<P>> buttons;
    private PageNode first;
    private ItemStack spaceFiller;
    private ItemStack prevButton;
    private ItemStack nextButton;
    private ItemStack emptyIndicator;

    public MultiPageGui(P plugin, String title, int rows, Collection<? extends Button<P>> buttons) {
        super(plugin);
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title cannot be null or empty");
        }
        this.title = title;
        if (rows <= 0 || rows > 5) {
            throw new IllegalArgumentException("rows out of range, must be between 1 - 5");
        }
        this.size = rows * 9 + 9;
        this.prevPageSlot = this.size - 9;
        this.nextPageSlot = this.size - 1;
        if (buttons == null) {
            throw new IllegalArgumentException("buttons cannot be null");
        }
        this.buttons = buttons;
    }

    public void calculatePages() {
        int maxSize = this.size - 9;
        int totalPages = this.buttons.size() / maxSize + (this.buttons.size() % maxSize > 0 ? 1 : 0);
        if (this.first == null) {
            this.first = this.createPage(1, totalPages);
        }
        if (totalPages == 0) {
            this.first.setEmpty();
            return;
        }
        int i = 0;
        int pageNum = 1;
        int slot = 0;
        PageNode last = null;
        for (Button<P> button : this.buttons) {
            if (i % maxSize == 0) {
                PageNode prev = last;
                if (last == null) {
                    last = this.first;
                } else {
                    if (last.next == null) {
                        last.next = this.createPage(pageNum, totalPages);
                    }
                    last = last.next;
                }
                last.setTitle(this.title + " (" + pageNum + "/" + totalPages + ")");
                last.clear();
                if (prev != null) {
                    last.previous = prev;
                    last.inventory.setItem(this.prevPageSlot, this.prevButton);
                    prev.next = last;
                    prev.inventory.setItem(this.nextPageSlot, this.nextButton);
                }
                slot = 0;
                ++pageNum;
            }
            this.set(last.inventory, slot, button);
            ++i;
            ++slot;
        }
        if (last != null) {
            last.resetNext();
        }
    }

    private PageNode createPage(int page, int total) {
        return new PageNode(InventoryBuilder.of(this.title + " (" + page + "/" + total + ")", this.size).fillRange(this.prevPageSlot, this.nextPageSlot + 1, this.getSpaceFiller()).build());
    }

    private ItemStack getSpaceFiller() {
        return this.spaceFiller != null ? this.spaceFiller : Items.WHITE_PANE.clone();
    }

    @Override
    public void open(Player ... players) {
        for (Player player : players) {
            player.openInventory(this.first.inventory);
        }
    }

    @Override
    public boolean isPart(Inventory inventory) {
        return this.first.isPart(inventory);
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
        PageNode node = this.first.find(clicked);
        if (node == null) {
            return;
        }
        int slot = event.getSlot();
        if (slot == this.nextPageSlot && node.next != null) {
            player.openInventory(node.next.inventory);
        } else if (slot == this.prevPageSlot && node.previous != null) {
            player.openInventory(node.previous.inventory);
        } else {
            Button button = this.get(clicked, slot);
            if (button == null) {
                return;
            }
            button.onClick(player);
        }
    }

    public Collection<? extends Button<P>> getButtons() {
        return this.buttons;
    }

    public void setSpaceFiller(ItemStack spaceFiller) {
        this.spaceFiller = spaceFiller;
    }

    public void setPrevButton(ItemStack prevButton) {
        this.prevButton = prevButton;
    }

    public void setNextButton(ItemStack nextButton) {
        this.nextButton = nextButton;
    }

    public void setEmptyIndicator(ItemStack emptyIndicator) {
        this.emptyIndicator = emptyIndicator;
    }

    private class PageNode {
        private final Inventory inventory;
        private PageNode previous;
        private PageNode next;

        PageNode(Inventory inventory) {
            this.inventory = inventory;
        }

        void setEmpty() {
            this.setTitle(MultiPageGui.this.title);
            ItemStack item = this.inventory.getItem(4);
            if (item != null && item.isSimilar(MultiPageGui.this.emptyIndicator)) {
                return;
            }
            this.clear();
            this.resetBottom();
            this.inventory.setItem(4, MultiPageGui.this.emptyIndicator);
            MultiPageGui.this.remove(this.inventory);
            this.resetNext();
        }

        void resetNext() {
            if (this.next == null) {
                return;
            }
            this.inventory.setItem(MultiPageGui.this.nextPageSlot, MultiPageGui.this.getSpaceFiller());
            this.forEach(node -> {
                if (node.equals(this)) {
                    return;
                }
                MultiPageGui.this.remove(node.inventory);
                Lists.newArrayList(node.inventory.getViewers()).forEach(HumanEntity::closeInventory);
            });
            this.next = null;
        }

        void setTitle(String title) {
            Inventories.setTitle(this.inventory, title);
        }

        void clear() {
            MultiPageGui.this.remove(this.inventory);
            for (int slot = 0; slot < this.inventory.getSize() - 9; ++slot) {
                this.inventory.setItem(slot, null);
            }
        }

        void resetBottom() {
            Slots.run(MultiPageGui.this.prevPageSlot, MultiPageGui.this.nextPageSlot + 1, slot -> this.inventory.setItem(slot.intValue(), MultiPageGui.this.getSpaceFiller()));
        }

        void forEach(Consumer<PageNode> consumer) {
            consumer.accept(this);
            if (this.next != null) {
                this.next.forEach(consumer);
            }
        }

        PageNode find(Inventory inventory) {
            if (this.inventory.equals(inventory)) {
                return this;
            }
            if (this.next != null) {
                return this.next.find(inventory);
            }
            return null;
        }

        boolean isPart(Inventory inventory) {
            return this.find(inventory) != null;
        }
    }
}

