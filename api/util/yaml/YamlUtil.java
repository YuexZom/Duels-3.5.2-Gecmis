/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConstructor
 *  org.bukkit.configuration.file.YamlRepresenter
 *  org.yaml.snakeyaml.DumperOptions
 *  org.yaml.snakeyaml.DumperOptions$FlowStyle
 *  org.yaml.snakeyaml.Yaml
 *  org.yaml.snakeyaml.constructor.BaseConstructor
 *  org.yaml.snakeyaml.nodes.Tag
 *  org.yaml.snakeyaml.representer.Representer
 */
package me.realized.duels.util.yaml;

import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public final class YamlUtil {
    private static final transient Yaml BUKKIT_YAML;
    private static final transient Yaml YAML;

    public static String yamlDump(Object object) {
        return YAML.dump(object);
    }

    public static String bukkitYamlDump(Object object) {
        return BUKKIT_YAML.dump(object);
    }

    public static <T> T yamlLoad(String yaml) {
        return (T)YAML.load(yaml);
    }

    public static <T> T bukkitYamlLoad(String yaml) {
        return (T)BUKKIT_YAML.load(yaml);
    }

    public static <T> T yamlLoadAs(String yaml, Class<T> type) {
        return (T)YAML.loadAs(yaml, type);
    }

    public static <T> T bukkitYamlLoadAs(String yaml, Class<T> type) {
        return (T)BUKKIT_YAML.loadAs(yaml, type);
    }

    private YamlUtil() {
    }

    static {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YamlRepresenter yamlRepresenter = new YamlRepresenter();
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        BUKKIT_YAML = new Yaml((BaseConstructor)new YamlBukkitConstructor(), (Representer)yamlRepresenter, options);
        YAML = new Yaml(options);
    }

    private static class YamlBukkitConstructor
    extends YamlConstructor {
        public YamlBukkitConstructor() {
            this.yamlConstructors.put(new Tag("tag:yaml.org,2002:org.bukkit.inventory.ItemStack"), this.yamlConstructors.get(Tag.MAP));
        }
    }
}

