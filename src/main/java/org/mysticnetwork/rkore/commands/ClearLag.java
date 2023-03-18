package org.mysticnetwork.rkore.commands;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.List;
import java.util.UUID;

import static org.mysticnetwork.rkore.runnable.AutoClearLag.countdown;


public class ClearLag extends SimpleCommand {


    public ClearLag(SimpleCommandGroup parent) {
        super("clearlag");
        setDescription(Settings.ClearLag.DESCRIPTION);
        setPermission(null);
        setPermissionMessage(Settings.ClearLag.NO_PERMISSION_MESSAGE
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.ClearLag.ALIASES);
        setMinArguments(0);
    }

    public int countdownTimeInt;
    private String intervalFormat = Settings.ClearLag.INTERVAL_FORMAT;


    protected void onCommand() {
        if (args.length > 0 && args[0].equalsIgnoreCase("time")) {
            if (sender instanceof Player) {
                if (sender.hasPermission(Settings.ClearLag.TIME_PERMISSION) || Settings.ClearLag.TIME_PERMISSION.equals("null")) {
                    if (countdown == -1) {
                        sender.sendMessage(Settings.ClearLag.NO_TIME
                                .replace("{prefix}", Settings.PREFIX));
                        return;
                    }
                    if (intervalFormat.equals("minutes")) {
                        countdownTimeInt = countdown / 60;
                        String minuteFormat = (countdownTimeInt == 60 ? "minute" : "minutes");
                        sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                                .replace("{prefix}", Settings.PREFIX)
                                .replace("{time}", countdownTimeInt + "")
                                .replace("{time-format}", minuteFormat));
                    }
                    if (intervalFormat.equals("hours")) {
                        countdownTimeInt = countdown / 3600;
                        String hourFormat = (countdownTimeInt == 3600 ? "hour" : "hours");
                        sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                                .replace("{prefix}", Settings.PREFIX)
                                .replace("{time}", countdownTimeInt + "")
                                .replace("{time-format}", hourFormat));
                    }
                    if (intervalFormat.equals("seconds")) {
                        countdownTimeInt = countdown;
                        String secondsFormat = (countdownTimeInt == 1 ? "second" : "seconds");
                        sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                                .replace("{prefix}", Settings.PREFIX)
                                .replace("{time}", countdownTimeInt + "")
                                .replace("{time-format}", secondsFormat));
                    }
                    return;
                } else {
                    sender.sendMessage(Settings.ClearLag.TIME_NO_PERMISSION
                            .replace("{prefix}", Settings.PREFIX));
                }
            } else {
                if (countdown == -1) {
                    sender.sendMessage(Settings.ClearLag.NO_TIME
                            .replace("{prefix}", Settings.PREFIX));
                    return;
                }
                if (intervalFormat.equals("minutes")) {
                    countdownTimeInt = countdown / 60;
                    String minuteFormat = (countdownTimeInt == 60 ? "minute" : "minutes");
                    sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{time}", countdownTimeInt + "")
                            .replace("{time-format}", minuteFormat));
                }
                if (intervalFormat.equals("hours")) {
                    countdownTimeInt = countdown / 3600;
                    String hourFormat = (countdownTimeInt == 3600 ? "hour" : "hours");
                    sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{time}", countdownTimeInt + "")
                            .replace("{time-format}", hourFormat));
                }
                if (intervalFormat.equals("seconds")) {
                    countdownTimeInt = countdown;
                    String secondsFormat = (countdownTimeInt == 1 ? "second" : "seconds");
                    sender.sendMessage(Settings.ClearLag.TIME_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{time}", countdownTimeInt + "")
                            .replace("{time-format}", secondsFormat));
                }
                return;
            }
        }
        if (sender.hasPermission(Settings.ClearLag.PERMISSION) || Settings.ClearLag.PERMISSION.equals("null")) {
            int removedEntities = 0;
            List<String> commandList = Settings.ClearLag.COMMANDS_ON_CLEAR;
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
            for (String commandString : commandList) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);

            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String totalRemovedEntities = String.valueOf((Integer) removedEntities);
                player.sendMessage(Settings.ClearLag.CLEARED_MESSAGE
                        .replace("{prefix}", Settings.PREFIX)
                        .replace("{amount}", totalRemovedEntities));
            } else {
                String totalRemovedEntities = String.valueOf((Integer) removedEntities);
                sender.sendMessage(Settings.ClearLag.CLEARED_MESSAGE
                        .replace("{prefix}", Settings.PREFIX)
                        .replace("{amount}", totalRemovedEntities));
            }
        } else {
            sender.sendMessage(Settings.ClearLag.NO_PERMISSION_MESSAGE
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
    protected List<String> tabComplete() {
        if (this.args.length == 1)
            return completeLastWord((Object[])new String[] { "time" });
        return completeLastWord("");
    }
}

