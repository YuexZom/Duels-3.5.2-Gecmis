/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.realized.duels.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import me.realized.duels.util.Loadable;
import me.realized.duels.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class Log {
    private static final String PLUGIN_WARN = "[%s] &c%s";
    private static final String PLUGIN_ERROR = "[%s] &4&l%s";
    private static final List<LogSource> sources = new ArrayList<LogSource>();

    private Log() {
    }

    public static void addSource(LogSource source) {
        sources.add(source);
    }

    public static void clearSources() {
        sources.clear();
    }

    public static void info(String s) {
        for (LogSource source : sources) {
            source.log(Level.INFO, s);
        }
    }

    public static void info(Loadable loadable, String s) {
        for (LogSource source : sources) {
            source.log(Level.INFO, loadable.getClass().getSimpleName() + ": " + s);
        }
    }

    public static void warn(String s) {
        for (LogSource source : sources) {
            if (source instanceof Plugin) {
                Bukkit.getConsoleSender().sendMessage(StringUtil.color(String.format(PLUGIN_WARN, ((Plugin)source).getName(), s)));
                continue;
            }
            source.log(Level.WARNING, s);
        }
    }

    public static void warn(Loadable loadable, String s) {
        Log.warn(loadable.getClass().getSimpleName() + ": " + s);
    }

    public static void error(String s, Throwable thrown) {
        for (LogSource source : sources) {
            if (source instanceof Plugin) {
                Bukkit.getConsoleSender().sendMessage(StringUtil.color(String.format(PLUGIN_ERROR, ((Plugin)source).getName(), s)));
                continue;
            }
            if (thrown != null) {
                source.log(Level.SEVERE, s, thrown);
                continue;
            }
            source.log(Level.SEVERE, s);
        }
    }

    public static void error(String s) {
        Log.error(s, null);
    }

    public static void error(Loadable loadable, String s, Throwable thrown) {
        Log.error(loadable.getClass().getSimpleName() + ": " + s, thrown);
    }

    public static void error(Loadable loadable, String s) {
        Log.error(loadable, s, null);
    }

    public static interface LogSource {
        public void log(Level var1, String var2);

        public void log(Level var1, String var2, Throwable var3);
    }
}

