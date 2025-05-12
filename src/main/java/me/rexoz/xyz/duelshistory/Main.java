package me.rexoz.xyz.duelshistory;

import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private final List<MatchData> allMatchHistory = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("macgecmisi").setExecutor(this);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        UUID winnerUuid = event.getWinner();
        UUID loserUuid = event.getLoser();
        Kit kit = event.getMatch().getKit();

        if (winnerUuid == null || loserUuid == null || kit == null) return;

        MatchData data = new MatchData(
                Bukkit.getOfflinePlayer(winnerUuid).getName(),
                Bukkit.getOfflinePlayer(loserUuid).getName(),
                kit.getName(),
                kit.getDisplayed(),
                new Date()
        );
        allMatchHistory.add(data);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (allMatchHistory.isEmpty()) {
            player.sendMessage("§cMaç geçmişi bulunamadı!");
            return true;
        }

        Inventory gui = Bukkit.createSingleInventory(null, "§bDuello Geçmişleri", 54 );

        for (int i = 0; i < Math.min(allMatchHistory.size(), 54); i++) {
            MatchData data = allMatchHistory.get(allMatchHistory.size() - 1 - i);
            ItemStack item = new ItemStack(data.kitIcon.getType());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§e" + data.kitName);
                List<String> lore = new ArrayList<>();
                lore.add("§7Kazanan: §a" + data.winner);
                lore.add("§7Kaybeden: §c" + data.loser);
                lore.add("§7Tarih: §f" + new SimpleDateFormat("dd.MM HH:mm").format(data.date));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        player.openInventory(gui);
        return true;
    }

    static class MatchData {
        final String winner;
        final String loser;
        final String kitName;
        final ItemStack kitIcon;
        final Date date;

        MatchData(String winner, String loser, String kitName, ItemStack kitIcon, Date date) {
            this.winner = winner;
            this.loser = loser;
            this.kitName = kitName;
            this.kitIcon = kitIcon;
            this.date = date;
        }
    }
}
