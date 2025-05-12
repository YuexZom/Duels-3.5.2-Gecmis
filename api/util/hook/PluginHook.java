/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginHook<P extends JavaPlugin> {
    protected final P plugin;
    private final String name;

    public PluginHook(P plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin(this.name);
    }
}

