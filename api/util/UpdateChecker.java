/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package me.realized.duels.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.BiConsumer;
import me.realized.duels.util.NumberUtil;
import org.bukkit.plugin.Plugin;

public final class UpdateChecker {
    private static final String API_URL = "https://api.spigotmc.org/legacy/update.php?resource=%s";
    private final Plugin plugin;
    private final int id;

    public UpdateChecker(Plugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void check(BiConsumer<Boolean, String> callback) {
        String currentVersion = this.plugin.getDescription().getVersion();
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(String.format(API_URL, this.id)).openStream()));){
                String latestVersion = reader.readLine();
                if (latestVersion == null) {
                    return;
                }
                boolean updateAvailable = NumberUtil.isLower(currentVersion, latestVersion);
                callback.accept(updateAvailable, updateAvailable ? latestVersion : currentVersion);
            } catch (IOException iOException) {
                // empty catch block
            }
        });
    }
}

