/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.realized.duels.api;

import java.util.UUID;
import me.realized.duels.DuelsPlugin;
import me.realized.duels.data.UserData;
import org.bukkit.entity.Player;

@Deprecated
public class DuelsAPI {
    @Deprecated
    public static UserData getUser(UUID uuid, boolean force) {
        return DuelsPlugin.getInstance().getUserManager().get(uuid);
    }

    @Deprecated
    public static UserData getUser(Player player, boolean force) {
        return DuelsPlugin.getInstance().getUserManager().get(player);
    }

    @Deprecated
    public static boolean isInMatch(Player player) {
        return DuelsPlugin.getInstance().getArenaManager().isInMatch(player);
    }

    @Deprecated
    public static String getVersion() {
        return DuelsPlugin.getInstance().getVersion();
    }
}

