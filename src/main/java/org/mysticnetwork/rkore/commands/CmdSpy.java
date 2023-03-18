package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.List;

public class CmdSpy extends SimpleCommand implements Listener {


    public CmdSpy(SimpleCommandGroup parent) {
        super("cmdspy");
        setDescription(Settings.CommandSpy.InGame.DESCRIPTION);
        setPermission(null);
        setPermissionMessage(Settings.CommandSpy.InGame.NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.CommandSpy.InGame.ALIASES);
        setMinArguments(0);
    }

    RKore plugin = (RKore) Bukkit.getPluginManager().getPlugin("RKore");
    List<Player> spyingPlayers = plugin.getSpyingPlayers();


    protected void onCommand() {
        if (sender instanceof Player) {
                if (!(sender.hasPermission(Settings.CommandSpy.InGame.PERMISSION) || Settings.CommandSpy.InGame.PERMISSION.equals("null"))) {
                    sender.sendMessage(Settings.CommandSpy.InGame.NO_PERMISSION
                            .replace("{prefix}", Settings.PREFIX));
                    return;
                }
            Player player = (Player) sender;
            if (args.length == 0) {
                if (spyingPlayers.contains(player)) {
                    spyingPlayers.remove(player);
                    player.sendMessage(Settings.CommandSpy.InGame.TOGGLE_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{toggle}", Settings.CommandSpy.InGame.OFF_PLACEHOLDER));
                } else {
                    spyingPlayers.add(player);
                    player.sendMessage(Settings.CommandSpy.InGame.TOGGLE_MESSAGE
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{toggle}", Settings.CommandSpy.InGame.ON_PLACEHOLDER));
                }
            }
        } else {
            sender.sendMessage(Settings.CommandSpy.InGame.ONLY_PLAYER
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
}