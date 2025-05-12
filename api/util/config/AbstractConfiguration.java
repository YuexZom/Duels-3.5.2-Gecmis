/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.FileConfigurationOptions
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.config;

import com.google.common.base.Charsets;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.realized.duels.util.Loadable;
import me.realized.duels.util.config.convert.Converter;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractConfiguration<P extends JavaPlugin>
implements Loadable {
    private static final String CONVERT_START = "[!] Converting your current configuration (%s) to the new version...";
    private static final String CONVERT_SAVE = "[!] Your old configuration was stored as %s.";
    private static final String CONVERT_DONE = "[!] Conversion complete!";
    private static final Pattern KEY_PATTERN = Pattern.compile("^([ ]*)([^ \"]+)[:].*$");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^([ ]*[#].*)|[ ]*$");
    protected final P plugin;
    private final String name;
    private final File file;
    private FileConfiguration configuration;

    public AbstractConfiguration(P plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder(), this.name);
    }

    @Override
    public void handleLoad() throws Exception {
        if (!this.file.exists()) {
            this.plugin.saveResource(this.name, true);
        }
        this.configuration = YamlConfiguration.loadConfiguration((File)this.file);
        this.loadValues(this.configuration);
    }

    @Override
    public void handleUnload() {
    }

    protected abstract void loadValues(FileConfiguration var1) throws Exception;

    protected int getLatestVersion() throws Exception {
        InputStream stream = this.plugin.getClass().getResourceAsStream("/" + this.name);
        if (stream == null) {
            throw new IllegalStateException(this.plugin.getName() + "'s jar file was replaced, but a reload was called! Please restart your server instead when updating this plugin.");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charsets.UTF_8));){
            int n = YamlConfiguration.loadConfiguration((Reader)reader).getInt("config-version", -1);
            return n;
        }
    }

    protected FileConfiguration convert(Converter converter) throws IOException {
        this.plugin.getLogger().info(String.format(CONVERT_START, this.name));
        HashMap<String, Object> oldValues = new HashMap<String, Object>();
        for (String key : this.configuration.getKeys(true)) {
            Object value;
            if (key.equals("config-version") || (value = this.configuration.get(key)) instanceof MemorySection) continue;
            oldValues.put(key, value);
        }
        if (converter != null) {
            converter.renamedKeys().forEach((old, changed) -> {
                Object previous = oldValues.get(old);
                if (previous != null) {
                    oldValues.remove(old);
                    oldValues.put((String)changed, previous);
                }
            });
        }
        String newName = this.name.replace(".yml", "") + "-" + System.currentTimeMillis() + ".yml";
        File copied = Files.copy(this.file.toPath(), new File(this.plugin.getDataFolder(), newName).toPath(), new CopyOption[0]).toFile();
        this.plugin.getLogger().info(String.format(CONVERT_SAVE, copied.getName()));
        this.plugin.saveResource(this.name, true);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(this.file), Charsets.UTF_8));){
            Matcher matcher;
            String line;
            LinkedListMultimap<String, ArrayList<Object>> comments = LinkedListMultimap.create();
            ArrayList<String> currentComments = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                matcher = KEY_PATTERN.matcher(line);
                if (matcher.find() && !COMMENT_PATTERN.matcher(line).matches()) {
                    comments.put(matcher.group(2), Lists.newArrayList(currentComments));
                    currentComments.clear();
                    continue;
                }
                if (!COMMENT_PATTERN.matcher(line).matches()) continue;
                currentComments.add(line);
            }
            this.configuration = YamlConfiguration.loadConfiguration((File)this.file);
            FileConfigurationOptions options = this.configuration.options();
            options.header(null);
            Method method = ReflectionUtil.getDeclaredMethodUnsafe(FileConfigurationOptions.class, "parseComments", Boolean.TYPE);
            if (method != null) {
                try {
                    method.invoke(options, false);
                } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                    // empty catch block
                }
            }
            for (Map.Entry entry : oldValues.entrySet()) {
                String key = (String)entry.getKey();
                Object value = this.configuration.get(key);
                if ((value == null || value instanceof MemorySection) && !this.transferredSections().stream().anyMatch(section -> key.startsWith(section + "."))) continue;
                this.configuration.set(key, entry.getValue());
            }
            ArrayList<String> commentlessData = Lists.newArrayList(this.configuration.saveToString().split("\n"));
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(this.file), Charsets.UTF_8));){
                for (String data : commentlessData) {
                    ArrayList commentData;
                    String key;
                    Collection result;
                    matcher = KEY_PATTERN.matcher(data);
                    if (matcher.find() && (result = comments.get(key = matcher.group(2))) != null && !(commentData = Lists.newArrayList(result)).isEmpty()) {
                        for (String comment : (List)commentData.get(0)) {
                            writer.write(comment);
                            writer.newLine();
                        }
                        commentData.remove(0);
                        comments.replaceValues(key, commentData);
                    }
                    writer.write(data);
                    if (commentlessData.indexOf(data) + 1 < commentlessData.size()) {
                        writer.newLine();
                        continue;
                    }
                    if (currentComments.isEmpty()) continue;
                    writer.newLine();
                }
                for (String comment : currentComments) {
                    writer.write(comment);
                    if (currentComments.indexOf(comment) + 1 >= currentComments.size()) continue;
                    writer.newLine();
                }
                writer.flush();
            }
            this.plugin.getLogger().info(CONVERT_DONE);
        }
        return this.configuration;
    }

    protected Set<String> transferredSections() {
        return Collections.emptySet();
    }
}

