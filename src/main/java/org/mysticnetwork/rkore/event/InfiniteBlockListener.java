package org.mysticnetwork.rkore.event;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfiniteBlockListener implements Listener {
    private ConcurrentHashMap<UUID, Double> playerSpentAmount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, BukkitTask> playerTasks = new ConcurrentHashMap<>();

    private RKore plugin;
    private Economy economy;

    public InfiniteBlockListener(RKore plugin) {
        this.plugin = plugin;
        this.economy = plugin.getEconomy();
    }
    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        ItemMeta meta = item.getItemMeta();
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (meta == null || meta.getDisplayName() == null || meta.getLore() == null) {
            return;
        }

        List<String> lore = meta.getLore();
        String lastLine = lore.get(lore.size() - 1);
        String hiddenBlockName = "";

        Pattern pattern = Pattern.compile("§.(?=§|$)");
        Matcher matcher = pattern.matcher(lastLine);
        while (matcher.find()) {
            hiddenBlockName += matcher.group().substring(1);
        }

        if (hiddenBlockName.isEmpty()) {
            return;
        }

        ConfigurationSection blockSection = plugin.getInfiniteBlocksConfig().getConfigurationSection("infinite-blocks." + hiddenBlockName);
        if (blockSection != null) {
            double pricePerUse = plugin.getInfiniteBlocksConfig().getDouble("infinite-blocks."+ hiddenBlockName + ".price-per-use");
            if (economy.getBalance(event.getPlayer()) >= pricePerUse) {
                economy.withdrawPlayer(event.getPlayer(), pricePerUse);
                playerSpentAmount.put(playerUUID, playerSpentAmount.getOrDefault(playerUUID, 0.0) + pricePerUse);
                BukkitTask existingTask = playerTasks.get(playerUUID);
                if (existingTask != null) {
                    existingTask.cancel();
                }

                BukkitTask newTask = new BukkitRunnable() {
                    int intervals = 1;

                    @Override
                    public void run() {
                        double totalSpent = playerSpentAmount.get(playerUUID);
                        event.getPlayer().sendMessage(Settings.InfiniteBlocks.SPENT
                                .replace("{prefix}", Settings.PREFIX)
                                .replace("{spent}", "" + totalSpent));

                        intervals++;

                        if (intervals > 1) {
                            playerSpentAmount.remove(playerUUID);
                            playerTasks.remove(playerUUID);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 200L, 200L);
               ItemStack newBucket = null;
                if (item.getType() == Material.LAVA_BUCKET) {
                    newBucket = new ItemStack(Material.LAVA_BUCKET);
                    newBucket.setItemMeta(meta);
                }
                if (item.getType() == Material.WATER_BUCKET) {
                    newBucket = new ItemStack(Material.WATER_BUCKET);
                    newBucket.setItemMeta(meta);
                }
                ItemStack finalNewBucket = newBucket;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().setItemInHand(finalNewBucket);
                        event.getPlayer().updateInventory();
                    }
                }.runTaskLater(plugin, 1L);
                playerTasks.put(playerUUID, newTask);
            } else {
                event.getPlayer().sendMessage(Settings.InfiniteBlocks.INSUFFICIENT_FUNDS
                        .replace("{prefix}", Settings.PREFIX));
                event.setCancelled(true);
            }
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (meta == null || meta.getDisplayName() == null || meta.getLore() == null) {
            return;
        }

        List<String> lore = meta.getLore();
        String lastLine = lore.get(lore.size() - 1);
        String hiddenBlockName = "";

        Pattern pattern = Pattern.compile("§.(?=§|$)");
        Matcher matcher = pattern.matcher(lastLine);
        while (matcher.find()) {
            hiddenBlockName += matcher.group().substring(1);
        }

        if (hiddenBlockName.isEmpty()) {
            return;
        }

            ConfigurationSection blockSection = plugin.getInfiniteBlocksConfig().getConfigurationSection("infinite-blocks." + hiddenBlockName);
            if (blockSection != null) {
                double pricePerUse = plugin.getInfiniteBlocksConfig().getDouble("infinite-blocks."+ hiddenBlockName + ".price-per-use");
                if (economy.getBalance(event.getPlayer()) >= pricePerUse) {
                    economy.withdrawPlayer(event.getPlayer(), pricePerUse);
                    playerSpentAmount.put(playerUUID, playerSpentAmount.getOrDefault(playerUUID, 0.0) + pricePerUse);
                    BukkitTask existingTask = playerTasks.get(playerUUID);
                    if (existingTask != null) {
                        existingTask.cancel();
                    }

                    BukkitTask newTask = new BukkitRunnable() {
                        int intervals = 1;

                        @Override
                        public void run() {
                            double totalSpent = playerSpentAmount.get(playerUUID);
                            event.getPlayer().sendMessage(Settings.InfiniteBlocks.SPENT
                                    .replace("{prefix}", Settings.PREFIX)
                                    .replace("{spent}", "" + totalSpent));

                            intervals++;

                            if (intervals > 1) {
                                playerSpentAmount.remove(playerUUID);
                                playerTasks.remove(playerUUID);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 200L, 200L);

                    playerTasks.put(playerUUID, newTask);
                    ItemStack newItem = item.clone();
                    event.getPlayer().getInventory().setItemInHand(newItem);
                } else {
                    event.getPlayer().sendMessage(Settings.InfiniteBlocks.INSUFFICIENT_FUNDS
                            .replace("{prefix}", Settings.PREFIX));
                    event.setCancelled(true);
                }
            }

        }
    }