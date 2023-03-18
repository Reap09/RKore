package org.mysticnetwork.rkore.commands;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

public class Discord extends SimpleCommand {

    public Discord(SimpleCommandGroup parent) {
        super("discord");
        setDescription(Settings.InfoCommands.DISCORD_DESCRIPTION);
        setPermission(null);
        setPermissionMessage(Settings.InfoCommands.DISCORD_NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.InfoCommands.DISCORD_ALIASES);
        setMinArguments(0);
    }

    protected void onCommand() {
        if (sender instanceof Player) {
            if (sender.hasPermission(Settings.InfoCommands.DISCORD_PERMISSION) || Settings.InfoCommands.DISCORD_PERMISSION.equals("null")) {
                Player player = (Player) sender;
                player.sendMessage((Settings.InfoCommands.DISCORD_MESSAGE)
                        .replace("{prefix}", Settings.PREFIX));
            } else {
                sender.sendMessage(Settings.InfoCommands.DISCORD_NO_PERMISSION
                        .replace("{prefix}", Settings.PREFIX));
            }
        } else {
            sender.sendMessage((Settings.InfoCommands.DISCORD_MESSAGE)
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
}
