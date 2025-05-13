package me.rexoz.xyz.duelshistory;

import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import me.realized.duels.api.kit.Kit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private final List<MatchData> allMatchHistory = new ArrayList<>();
    private final Map<UUID, Double> playerBalanceBeforeMatch = new HashMap<>();
    private final Map<UUID, Long> matchStartTimes = new HashMap<>();
    private final Map<UUID, Long> matchEndTimes = new HashMap<>();
    private Economy economy;

    private File matchDataFile;
    private YamlConfiguration matchDataConfig;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("macgecmisi").setExecutor(this);

        loadMatchHistory();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : event.getMatch().getPlayers()) {
            double balance = getEconomy().getBalance(player);
            playerBalanceBeforeMatch.put(player.getUniqueId(), balance);
            matchStartTimes.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        UUID winnerUuid = event.getWinner();
        UUID loserUuid = event.getLoser();
        Kit kit = event.getMatch().getKit();

        if (winnerUuid == null || loserUuid == null || kit == null) return;

        Player winner = Bukkit.getPlayer(winnerUuid);
        if (winner == null) return;

        double betAmount = 0.0;
        Double beforeBalance = playerBalanceBeforeMatch.get(winnerUuid);
        if (beforeBalance != null) {
            double afterBalance = getEconomy().getBalance(winner);
            double diff = afterBalance - beforeBalance;
            if (diff > 0) betAmount = diff;
        }

        matchEndTimes.put(winnerUuid, System.currentTimeMillis());
        matchEndTimes.put(loserUuid, System.currentTimeMillis());

        long matchDuration = matchEndTimes.get(winnerUuid) - matchStartTimes.get(winnerUuid);
        String durationFormatted = formatDuration(matchDuration);

        MatchData data = new MatchData(
                Bukkit.getOfflinePlayer(winnerUuid).getName(),
                Bukkit.getOfflinePlayer(loserUuid).getName(),
                kit.getName(),
                betAmount,
                kit.getDisplayed(),
                new Date(),
                durationFormatted
        );

        allMatchHistory.add(data);
        saveMatchHistory();
        playerBalanceBeforeMatch.remove(winnerUuid);
        playerBalanceBeforeMatch.remove(loserUuid);
    }

    private void saveMatchHistory() {
        matchDataFile = new File(getDataFolder(), "matchHistory.yml");
        if (!matchDataFile.exists()) {
            try {
                getDataFolder().mkdirs();
                matchDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        matchDataConfig = YamlConfiguration.loadConfiguration(matchDataFile);
        List<Map<String, Object>> historyList = new ArrayList<>();

        for (MatchData data : allMatchHistory) {
            Map<String, Object> matchMap = new HashMap<>();
            matchMap.put("winner", data.winner);
            matchMap.put("loser", data.loser);
            matchMap.put("kitName", data.kitName);
            matchMap.put("betAmount", data.betAmount);
            matchMap.put("kitIcon", data.kitIcon.getType().name());
            matchMap.put("date", data.date.getTime());
            matchMap.put("matchDuration", data.matchDuration);
            historyList.add(matchMap);
        }

        matchDataConfig.set("matchHistory", historyList);

        try {
            matchDataConfig.save(matchDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMatchHistory() {
        matchDataFile = new File(getDataFolder(), "matchHistory.yml");
        if (!matchDataFile.exists()) return;

        matchDataConfig = YamlConfiguration.loadConfiguration(matchDataFile);
        List<Map<?, ?>> historyList = matchDataConfig.getMapList("matchHistory");

        for (Map<?, ?> map : historyList) {
            String winner = (String) map.get("winner");
            String loser = (String) map.get("loser");
            String kitName = (String) map.get("kitName");

            double betAmount = 0.0;
            Object betObj = map.get("betAmount");
            if (betObj instanceof Number) betAmount = ((Number) betObj).doubleValue();

            Date date = new Date();
            Object dateObj = map.get("date");
            if (dateObj instanceof Number) date = new Date(((Number) dateObj).longValue());

            Material mat = Material.STONE;
            Object matObj = map.get("kitIcon");
            if (matObj instanceof String) {
                try {
                    mat = Material.valueOf((String) matObj);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Geçersiz materyal: " + matObj + ", STONE varsayılan olarak atanıyor.");
                }
            }

            String duration = (String) map.get("matchDuration");

            MatchData data = new MatchData(winner, loser, kitName, betAmount, new ItemStack(mat), date, duration);
            allMatchHistory.add(data);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (allMatchHistory.isEmpty()) {
            player.sendMessage("§cDuello geçmişi bulunamadı!");
            return true;
        }

        Inventory gui = Bukkit.createSingleInventory(null, "§8Son Duellolar", 149);

        for (int i = 0; i < Math.min(allMatchHistory.size(), 149); i++) {
            MatchData data = allMatchHistory.get(allMatchHistory.size() - 1 - i);
            ItemStack item = new ItemStack(data.kitIcon.getType());
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName("§eDuello #" + (i + 1));
                List<String> lore = new ArrayList<>();
                lore.add("");
                boolean winnerIsRed = new Random().nextBoolean();
                String redPlayer = winnerIsRed ? data.winner : data.loser;
                String bluePlayer = winnerIsRed ? data.loser : data.winner;
                lore.add("§7Kazanan: §f" + data.winner);
                lore.add("§7Ödül: §f" + data.betAmount + " Kinas");
                lore.add("§7Süre: §f" + data.matchDuration);
                lore.add("§7Kit: §f" + data.kitName);
                lore.add("");
                lore.add("§cKırmızı Takım:");
                lore.add("§7" + redPlayer );
                lore.add("§3Mavi Takım: §f");
                lore.add("§7" + bluePlayer );
                lore.add("");
                lore.add("§7Tarih: §f" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(data.date));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        player.openInventory(gui);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;
        if (event.getView().getTitle().equals("§8Son Duellolar")) event.setCancelled(true);
    }

    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    static class MatchData {
        final String winner;
        final String loser;
        final String kitName;
        final double betAmount;
        final ItemStack kitIcon;
        final Date date;
        final String matchDuration;

        MatchData(String winner, String loser, String kitName, double betAmount, ItemStack kitIcon, Date date, String matchDuration) {
            this.winner = winner;
            this.loser = loser;
            this.kitName = kitName;
            this.betAmount = betAmount;
            this.kitIcon = kitIcon;
            this.date = date;
            this.matchDuration = matchDuration;
        }
    }
}
