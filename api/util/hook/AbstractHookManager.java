/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.hook;

import java.util.HashMap;
import java.util.Map;
import me.realized.duels.util.hook.PluginHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractHookManager<P extends JavaPlugin> {
    protected final P plugin;
    private final Map<Class<? extends PluginHook<P>>, PluginHook<P>> hooks = new HashMap<Class<? extends PluginHook<P>>, PluginHook<P>>();

    public AbstractHookManager(P plugin) {
        this.plugin = plugin;
    }

    protected void register(String name, Class<? extends PluginHook<P>> clazz) {
        Plugin target = Bukkit.getPluginManager().getPlugin(name);
        if (target == null || !target.isEnabled()) {
            return;
        }
        try {
            if (this.hooks.putIfAbsent(clazz, clazz.getConstructor(this.plugin.getClass()).newInstance(this.plugin)) != null) {
                this.plugin.getLogger().warning("Failed to hook into " + name + ": There was already a hook registered with same name");
                return;
            }
            this.plugin.getLogger().info("Successfully hooked into '" + name + "'!");
        } catch (Throwable throwable) {
            if (throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            this.plugin.getLogger().warning("Failed to hook into " + name + ": " + throwable.getMessage());
        }
    }

    public <T extends PluginHook<P>> T getHook(Class<T> clazz) {
        return (T)(clazz != null ? (PluginHook)clazz.cast(this.hooks.get(clazz)) : null);
    }
}

