/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package me.realized.duels.api.event.queue.sign;

import me.realized.duels.api.event.queue.sign.QueueSignEvent;
import me.realized.duels.api.queue.sign.QueueSign;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QueueSignRemoveEvent
extends QueueSignEvent {
    private static final HandlerList handlers = new HandlerList();

    public QueueSignRemoveEvent(@NotNull Player source, @NotNull QueueSign queueSign) {
        super(source, queueSign);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

