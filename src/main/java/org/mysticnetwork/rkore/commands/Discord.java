package org.mysticnetwork.rkore.commands;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

public class Discord extends SimpleCommand {

    public Discord(SimpleCommandGroup parent) {
        super("discord");
        setDescription(Settings.InfoCommands.DISCORD_DESCRIPTION);
        setPermission(Settings.InfoCommands.DISCORD_PERMISSION);
        setPermissionMessage(Settings.InfoCommands.DISCORD_NO_PERMISSION);
        setMinArguments(0);
    }

    protected void onCommand() {
        Player player = (Player) sender;
        player.sendMessage((Settings.InfoCommands.DISCORD_MESSAGE).replace("{prefix}", Settings.PREFIX));
    }
}
