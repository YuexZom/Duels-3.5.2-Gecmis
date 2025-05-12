/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package me.realized.duels.api.event.queue;

import java.util.Objects;
import me.realized.duels.api.event.queue.QueueEvent;
import me.realized.duels.api.queue.DQueue;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QueueLeaveEvent
extends QueueEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player source;

    public QueueLeaveEvent(@NotNull Player source, @NotNull DQueue queue) {
        super((CommandSender)source, queue);
        Objects.requireNonNull(source, "source");
        this.source = source;
    }

    @NotNull
    public Player getSource() {
        return this.source;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

