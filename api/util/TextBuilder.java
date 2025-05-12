/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.entity.Player
 */
package me.realized.duels.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class TextBuilder {
    private final List<BaseComponent> list = new ArrayList<BaseComponent>();

    private TextBuilder(String base, ClickEvent.Action clickAction, String clickValue, HoverEvent.Action hoverAction, String hoverValue) {
        if (base == null) {
            return;
        }
        Arrays.stream(TextComponent.fromLegacyText((String)base)).forEach(component -> {
            if (clickValue != null) {
                component.setClickEvent(new ClickEvent(clickAction, clickValue));
            }
            if (hoverValue != null) {
                component.setHoverEvent(new HoverEvent(hoverAction, TextComponent.fromLegacyText((String)hoverValue)));
            }
            this.list.add((BaseComponent)component);
        });
    }

    public static TextBuilder of(String base, ClickEvent.Action clickAction, String clickValue, HoverEvent.Action hoverAction, String hoverValue) {
        return new TextBuilder(base, clickAction, clickValue, hoverAction, hoverValue);
    }

    public static TextBuilder of(String base) {
        return TextBuilder.of(base, null, null, null, null);
    }

    public TextBuilder add(String text) {
        if (text == null) {
            return this;
        }
        this.list.addAll(Arrays.asList(TextComponent.fromLegacyText((String)text)));
        return this;
    }

    public TextBuilder add(String text, ClickEvent.Action action, String value) {
        if (text == null || value == null) {
            return this;
        }
        Arrays.stream(TextComponent.fromLegacyText((String)text)).forEach(component -> {
            component.setClickEvent(new ClickEvent(action, value));
            this.list.add((BaseComponent)component);
        });
        return this;
    }

    public TextBuilder add(String text, HoverEvent.Action action, String value) {
        if (text == null || value == null) {
            return this;
        }
        Arrays.stream(TextComponent.fromLegacyText((String)text)).forEach(component -> {
            component.setHoverEvent(new HoverEvent(action, TextComponent.fromLegacyText((String)value)));
            this.list.add((BaseComponent)component);
        });
        return this;
    }

    public TextBuilder add(String text, ClickEvent.Action clickAction, String clickValue, HoverEvent.Action hoverAction, String hoverValue) {
        if (text == null) {
            return this;
        }
        Arrays.stream(TextComponent.fromLegacyText((String)text)).forEach(component -> {
            if (clickValue != null) {
                component.setClickEvent(new ClickEvent(clickAction, clickValue));
            }
            if (hoverValue != null) {
                component.setHoverEvent(new HoverEvent(hoverAction, TextComponent.fromLegacyText((String)hoverValue)));
            }
            this.list.add((BaseComponent)component);
        });
        return this;
    }

    public TextBuilder setClickEvent(ClickEvent.Action action, String value) {
        if (value == null) {
            return this;
        }
        this.list.forEach(component -> component.setClickEvent(new ClickEvent(action, value)));
        return this;
    }

    public TextBuilder setHoverEvent(HoverEvent.Action action, String value) {
        if (value == null) {
            return this;
        }
        this.list.forEach(component -> component.setHoverEvent(new HoverEvent(action, TextComponent.fromLegacyText((String)value))));
        return this;
    }

    public void send(Collection<Player> players) {
        BaseComponent[] message = this.list.toArray(new BaseComponent[0]);
        players.forEach(player -> {
            if (player.isOnline()) {
                player.spigot().sendMessage(message);
            }
        });
    }

    public void send(Player ... players) {
        this.send(Arrays.asList(players));
    }
}

