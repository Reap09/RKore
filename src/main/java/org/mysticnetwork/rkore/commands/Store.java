package org.mysticnetwork.rkore.commands;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

public class Store extends SimpleCommand {

    public Store(SimpleCommandGroup parent) {
        super("store");
        setDescription(Settings.InfoCommands.STORE_DESCRIPTION);
        setPermission(Settings.InfoCommands.STORE_PERMISSION);
        setPermissionMessage(Settings.InfoCommands.STORE_NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.InfoCommands.STORE_ALIASES);
        setMinArguments(0);
    }

    protected void onCommand() {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage((Settings.InfoCommands.STORE_MESSAGE)
                    .replace("{prefix}", Settings.PREFIX));
        } else {
            sender.sendMessage((Settings.InfoCommands.STORE_MESSAGE)
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
}