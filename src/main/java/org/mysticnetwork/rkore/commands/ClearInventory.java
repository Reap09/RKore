package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.settings.Settings;

import java.sql.Timestamp;
import java.util.*;


public class ClearInventory extends SimpleCommand {

    public ClearInventory(SimpleCommandGroup parent) {
        super("clearinventory");
        setDescription(Settings.ClearInventory.DESCRIPTION);
        setPermission(null);
        setPermissionMessage(Settings.ClearInventory.NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.ClearInventory.ALIASES);
        setMinArguments(0);
    }

    RKore plugin = (RKore) Bukkit.getPluginManager().getPlugin("RKore");
    Map<UUID, Timestamp> confirmations = plugin.getConfirmations();
    List<String> commandList = Settings.ClearInventory.CONSOLE_CMDS_ON_CLEAR;

    protected void onCommand() {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.ClearInventory.ONLY_PLAYER
                    .replace("{prefix}", Settings.PREFIX));
            return;
        }
        if (!(sender.hasPermission(Settings.ClearInventory.PERMISSION) || Settings.ClearInventory.PERMISSION.equals("none"))) {
            sender.sendMessage(Settings.ClearInventory.NO_PERMISSION
                    .replace("{prefix}", Settings.PREFIX));
            return;
        }
        Player player = (Player) sender;
        Player target;
        if (player.hasPermission(Settings.ClearInventory.BYPASS_PERMISSION)) {
            if (args.length > 0) {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Settings.ClearInventory.INVALID_PLAYER
                            .replace("{target}", args[0])
                            .replace("{prefix}", Settings.PREFIX));
                    return;
                }
            } else {
                target = player;
            }
        } else target = player;
        ItemStack item = null;
        if (args.length > 1) {
            Material material = Material.getMaterial(args[1].toUpperCase());
            if (material == null) {
                sender.sendMessage(Settings.ClearInventory.INVALID_ITEMS.replace("{prefix}", Settings.PREFIX).replace("{item}", args[1]));
                return;
            }
            item = new ItemStack(material);
        }
        if (!player.hasPermission(Settings.ClearInventory.BYPASS_PERMISSION)) {
            if (confirmations.containsKey(player.getUniqueId())) {
                long timeLeft = confirmations.get(player.getUniqueId()).getTime() - System.currentTimeMillis();
                if (timeLeft > 0) {
                    if (item == null) {
                        target.getInventory().clear();
                        target.getInventory().setArmorContents(null);
                        for (String commandString : commandList) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString.replace("{player}", target.getName()));

                        }
                        sender.sendMessage((target == player ? (Settings.ClearInventory.CLEAR_SELF
                                .replace("{prefix}", Settings.PREFIX)) : (Settings.ClearInventory.CLEAR_TARGET)
                                .replace("{prefix}", Settings.PREFIX)
                                .replace("{target}", args[0])));
                        if (target != player) {
                            target.sendMessage(Settings.ClearInventory.CLEARED_TARGET
                                    .replace("{prefix}", Settings.PREFIX));
                        }
                    } else {
                        if (args.length > 2) {
                            try {
                                int amount = Integer.parseInt(args[2]);
                                if (amount <= 0) {
                                    sender.sendMessage(Settings.ClearInventory.INVALID_AMOUNT
                                            .replace("{prefix}", Settings.PREFIX));
                                    return;
                                }
                                item.setAmount(amount);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Settings.ClearInventory.INVALID_AMOUNT
                                        .replace("{prefix}", Settings.PREFIX));
                                return;
                            }
                        } else {
                            int amount = 0;
                            for (ItemStack i : target.getInventory().getContents()) {
                                if (i != null && i.getType() == item.getType()) {
                                    amount += i.getAmount();
                                }
                            }
                            item.setAmount(amount);
                        }
                        target.getInventory().removeItem(item);
                        for (String commandString : commandList) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString.replace("{player}", target.getName()));

                        }
                        sender.sendMessage((target == sender ? Settings.ClearInventory.CLEAR_ITEM_SELF
                                .replace("{prefix}", Settings.PREFIX) : Settings.ClearInventory.CLEAR_ITEM_TARGET
                                .replace("{prefix}", Settings.PREFIX))
                                .replace("{item}", item.getType().name().toLowerCase())
                                .replace("{target}", args[0]));
                        if (args[0].equals(target.getName()) && !args[0].equals(sender.getName())) {
                            target.sendMessage(Settings.ClearInventory.REMOVED_ITEM
                                    .replace("{prefix}", Settings.PREFIX)
                                    .replace("{item}", item.getType().name().toLowerCase()));
                        }

                    }
                    confirmations.remove(player.getUniqueId());
                    return;
                } else {
                    confirmations.remove(player.getUniqueId());
                }
            }
            confirmations.put(player.getUniqueId(), new Timestamp(System.currentTimeMillis() + (Long.parseLong(Settings.ClearInventory.CONFIRM_TIME) * 1000)));
            sender.sendMessage(Settings.ClearInventory.CONFIRM_MESSAGE
                    .replace("{time}", Settings.ClearInventory.CONFIRM_TIME)
                    .replace("{target}", (target == player ? "your" : target.getName() + "'s"))
                    .replace("{prefix}", Settings.PREFIX));

        } else {
            if (item == null) {
                target.getInventory().clear();
                target.getInventory().setArmorContents(null);
                for (String commandString : commandList) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString.replace("{player}", target.getName()));

                }
                sender.sendMessage((target == player ? (Settings.ClearInventory.CLEAR_SELF
                        .replace("{prefix}", Settings.PREFIX)) : (Settings.ClearInventory.CLEAR_TARGET)
                        .replace("{prefix}", Settings.PREFIX)
                        .replace("{target}", args[0])));
                if (target != player) {
                    target.sendMessage(Settings.ClearInventory.CLEARED_TARGET.replace("{prefix}", Settings.PREFIX));
                }
            } else {
                if (args.length > 2) {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        if (amount <= 0) {
                            sender.sendMessage(Settings.ClearInventory.INVALID_AMOUNT
                                    .replace("{prefix}", Settings.PREFIX));
                            return;
                        }
                        item.setAmount(amount);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Settings.ClearInventory.INVALID_AMOUNT
                                .replace("{prefix}", Settings.PREFIX));
                        return;
                    }
                } else {
                    int amount = 0;
                    for (ItemStack i : target.getInventory().getContents()) {
                        if (i != null && i.getType() == item.getType()) {
                            amount += i.getAmount();
                        }
                    }
                    item.setAmount(amount);
                }
                target.getInventory().removeItem(item);
                for (String commandString : commandList) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString.replace("{player}", target.getName()));

                }
                sender.sendMessage((target == sender ? Settings.ClearInventory.CLEAR_ITEM_SELF
                        .replace("{prefix}", Settings.PREFIX) : Settings.ClearInventory.CLEAR_ITEM_TARGET
                        .replace("{prefix}", Settings.PREFIX))
                        .replace("{item}", item.getType().name().toLowerCase())
                        .replace("{target}", args[0]));
                if (args[0].equals(target.getName()) && !args[0].equals(sender.getName())) {
                    target.sendMessage(Settings.ClearInventory.REMOVED_ITEM
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{item}", item.getType().name().toLowerCase()));
                }

            }
        }
    }


    @Override
    protected List<String> tabComplete() {
        if (this.args.length == 1)
            if (this.getPlayer().hasPermission(Settings.ClearInventory.BYPASS_PERMISSION)) {
                return completeLastWordPlayerNames();
            } else {
                return Collections.singletonList(this.getPlayer().getDisplayName());
            }
        if (args.length == 2) {
            ArrayList<String> materials = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material != Material.AIR) {
                    materials.add(material.name().toLowerCase());
                }
            }
            return StringUtil.copyPartialMatches(args[1].toLowerCase(), materials, new ArrayList<>());
        }
        return null;
    }
}