package org.mysticnetwork.rkore.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.List;


public class AutoClearLag implements Runnable {
    private int interval = Settings.ClearLag.INTERVAL;
    private String intervalFormat = Settings.ClearLag.INTERVAL_FORMAT;
    private List<String> intervalMessages = Settings.ClearLag.INTERVAL_MESSAGES;
    int removedEntities;
    public static int countdown = -1;

    @Override
    public void run() {
        try {
            if (intervalFormat.equals("minutes") && countdown == -1) {
                countdown = interval * 60;
            }
            if (intervalFormat.equals("hours") && countdown == -1) {
                countdown = interval * 3600;
            }
            if (intervalFormat.equals("seconds") && countdown == -1) {
                countdown = interval;
            }
            removedEntities = 0;
            List<String> commandList = Settings.ClearLag.COMMANDS_ON_CLEAR;
            if (countdown == 0) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (!(entity instanceof Player) && !(entity instanceof ArmorStand) && !Settings.ClearLag.ALLOWED_ENTITIES.contains(entity.getType().name())) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;
                                Material material = item.getItemStack().getType();
                                if (!Settings.ClearLag.ALLOWED_ENTITIES.contains(material.name())) {
                                    entity.remove();
                                    removedEntities++;
                                }
                            } else {
                                entity.remove();
                                removedEntities++;
                            }
                        }
                    }
                }
                countdown = -1;
                for (String commandString : commandList) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);
                }
                String totalRemovedEntities = String.valueOf((Integer) removedEntities);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Settings.ClearLag.CLEARED_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{amount}", totalRemovedEntities));
                }
            } else {
                for (String message : intervalMessages) {
                    String[] parts = message.split(":");
                    int time = Integer.parseInt(parts[0]);
                    if (countdown == time) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', parts[1])
                                    .replace("{prefix}", Settings.PREFIX));
                        }
                    }

                }
                countdown--;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}