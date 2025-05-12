/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.realized.duels.util.compat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import me.realized.duels.util.StringUtil;
import me.realized.duels.util.compat.CompatUtil;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.entity.Player;

public final class Titles {
    private static Method GET_HANDLE;
    private static Field PLAYER_CONNECTION;
    private static Method SEND_PACKET;
    private static Class<?> TITLE_ACTIONS;
    private static Constructor<?> TITLE_PACKET_FULL_CONSTRUCTOR;
    private static Constructor<?> TITLE_PACKET_CONSTRUCTOR;
    private static Class<?> CHAT_SERIALIZER;

    private Titles() {
    }

    public static void send(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (CompatUtil.hasSendTitle()) {
            player.sendTitle(StringUtil.color(title), subtitle != null ? StringUtil.color(subtitle) : null, fadeIn, stay, fadeOut);
        } else {
            try {
                Object connection = PLAYER_CONNECTION.get(GET_HANDLE.invoke(player, new Object[0]));
                ?[] actions = TITLE_ACTIONS.getEnumConstants();
                SEND_PACKET.invoke(connection, TITLE_PACKET_FULL_CONSTRUCTOR.newInstance(actions[2], null, fadeIn, stay, fadeOut));
                Object text = CHAT_SERIALIZER.getConstructor(String.class).newInstance(StringUtil.color(title));
                SEND_PACKET.invoke(connection, TITLE_PACKET_CONSTRUCTOR.newInstance(actions[0], text));
                if (subtitle != null) {
                    text = CHAT_SERIALIZER.getConstructor(String.class).newInstance(StringUtil.color(subtitle));
                    SEND_PACKET.invoke(connection, TITLE_PACKET_CONSTRUCTOR.newInstance(actions[1], text));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static {
        if (!CompatUtil.hasSendTitle()) {
            Class<?> CB_PLAYER = ReflectionUtil.getCBClass("entity.CraftPlayer");
            GET_HANDLE = ReflectionUtil.getMethod(CB_PLAYER, "getHandle", new Class[0]);
            Class<?> NMS_PLAYER = ReflectionUtil.getNMSClass("EntityPlayer");
            PLAYER_CONNECTION = ReflectionUtil.getField(NMS_PLAYER, "playerConnection");
            Class<?> NMS_PLAYER_CONNECTION = ReflectionUtil.getNMSClass("PlayerConnection");
            Class<?> NMS_PACKET = ReflectionUtil.getNMSClass("Packet");
            SEND_PACKET = ReflectionUtil.getMethod(NMS_PLAYER_CONNECTION, "sendPacket", NMS_PACKET);
            Class<?> TITLE_PACKET = ReflectionUtil.getNMSClass("PacketPlayOutTitle");
            Class<?> CHAT_COMPONENT = ReflectionUtil.getNMSClass("IChatBaseComponent");
            TITLE_ACTIONS = ReflectionUtil.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            if (TITLE_ACTIONS == null) {
                TITLE_ACTIONS = ReflectionUtil.getNMSClass("EnumTitleAction", false);
            }
            TITLE_PACKET_FULL_CONSTRUCTOR = ReflectionUtil.getConstructor(TITLE_PACKET, TITLE_ACTIONS, CHAT_COMPONENT, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            TITLE_PACKET_CONSTRUCTOR = ReflectionUtil.getConstructor(TITLE_PACKET, TITLE_ACTIONS, CHAT_COMPONENT);
            CHAT_SERIALIZER = ReflectionUtil.getNMSClass("ChatComponentText");
        }
    }
}

