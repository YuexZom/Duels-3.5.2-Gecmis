/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.realized.duels.util.command;

import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.realized.duels.api.command.SubCommand;
import me.realized.duels.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand<P extends JavaPlugin>
implements TabCompleter {
    protected final P plugin;
    private final String name;
    private final String usage;
    private final String description;
    private final String permission;
    private final boolean playerOnly;
    private final int length;
    private final List<String> aliases;
    private Map<String, AbstractCommand<P>> children;

    protected AbstractCommand(P plugin, String name, String usage, String description, String permission, int length, boolean playerOnly, String ... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
        this.length = length;
        this.playerOnly = playerOnly;
        ArrayList<String> names = Lists.newArrayList(aliases);
        names.add(name);
        this.aliases = Collections.unmodifiableList(names);
    }

    protected AbstractCommand(P plugin, SubCommand sub) {
        this(plugin, sub.getName(), sub.getUsage(), sub.getDescription(), sub.getPermission(), sub.getLength(), sub.isPlayerOnly(), sub.getAliases());
    }

    @SafeVarargs
    public final void child(AbstractCommand<P> ... commands) {
        if (commands == null || commands.length == 0) {
            return;
        }
        if (this.children == null) {
            this.children = new HashMap<String, AbstractCommand<P>>();
        }
        for (AbstractCommand<P> child : commands) {
            for (String alias : child.getAliases()) {
                if (this.children.containsKey(alias = alias.toLowerCase())) continue;
                this.children.put(alias, child);
            }
        }
    }

    public boolean isChild(String name) {
        return this.children != null && this.children.get(name.toLowerCase()) != null;
    }

    private PluginCommand getCommand() {
        PluginCommand pluginCommand = this.plugin.getCommand(this.name);
        if (pluginCommand == null) {
            throw new IllegalArgumentException("Command is not registered in plugin.yml");
        }
        return pluginCommand;
    }

    public final void register() {
        PluginCommand pluginCommand = this.getCommand();
        pluginCommand.setExecutor((sender, command, label, args) -> {
            if (this.isPlayerOnly() && !(sender instanceof Player)) {
                this.handleMessage(sender, MessageType.PLAYER_ONLY, new String[0]);
                return true;
            }
            if (this.permission != null && !sender.hasPermission(this.getPermission())) {
                this.handleMessage(sender, MessageType.NO_PERMISSION, this.permission);
                return true;
            }
            if (this.executeFirst(sender, label, args)) {
                return true;
            }
            if (args.length > 0 && this.children != null) {
                AbstractCommand<P> child = this.children.get(args[0].toLowerCase());
                if (child == null) {
                    this.handleMessage(sender, MessageType.SUB_COMMAND_INVALID, label, args[0]);
                    return true;
                }
                if (child.isPlayerOnly() && !(sender instanceof Player)) {
                    this.handleMessage(sender, MessageType.PLAYER_ONLY, new String[0]);
                    return true;
                }
                if (child.getPermission() != null && !sender.hasPermission(child.getPermission())) {
                    this.handleMessage(sender, MessageType.NO_PERMISSION, child.getPermission());
                    return true;
                }
                if (args.length < child.length) {
                    this.handleMessage(sender, MessageType.SUB_COMMAND_USAGE, label, child.getUsage(), child.getDescription());
                    return true;
                }
                child.execute(sender, label, args);
                return true;
            }
            this.execute(sender, label, args);
            return true;
        });
        pluginCommand.setTabCompleter((sender, command, alias, args) -> {
            List<String> result;
            AbstractCommand<P> child;
            if (this.children != null && args.length > 1 && (child = this.children.get(args[0].toLowerCase())) != null && (result = child.onTabComplete(sender, command, alias, args)) != null) {
                return result;
            }
            return this.onTabComplete(sender, command, alias, args);
        });
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return null;
        }
        if (args.length == 1 && this.children != null) {
            return this.children.values().stream().map(AbstractCommand::getName).filter(childName -> childName.startsWith(args[0].toLowerCase())).distinct().sorted(String::compareTo).collect(Collectors.toList());
        }
        return null;
    }

    protected boolean executeFirst(CommandSender sender, String label, String[] args) {
        return false;
    }

    protected abstract void execute(CommandSender var1, String var2, String[] var3);

    protected void handleMessage(CommandSender sender, MessageType type, String ... args) {
        sender.sendMessage(type.defaultMessage.format(args));
    }

    public String getName() {
        return this.name;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public int getLength() {
        return this.length;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    protected static enum MessageType {
        PLAYER_ONLY("&cThis command can only be executed by a player!"),
        NO_PERMISSION("&cYou need the following permission: {0}"),
        SUB_COMMAND_INVALID("&c''{1}'' is not a valid sub command. Type /{0} for help."),
        SUB_COMMAND_USAGE("&cUsage: /{0} {1} - {2}");

        private final MessageFormat defaultMessage;

        private MessageType(String defaultMessage) {
            this.defaultMessage = new MessageFormat(StringUtil.color(defaultMessage));
        }
    }
}

