package org.mysticnetwork.rkore.commands;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

public class Website extends SimpleCommand {

    public Website(SimpleCommandGroup parent) {
        super("website");
        setDescription(Settings.InfoCommands.WEBSITE_DESCRIPTION);
        setPermission(Settings.InfoCommands.WEBSITE_PERMISSION);
        setPermissionMessage(Settings.InfoCommands.WEBSITE_NO_PERMISSION);
        setMinArguments(0);
    }

    protected void onCommand() {
        Player player = (Player) sender;
        player.sendMessage((Settings.InfoCommands.WEBSITE_MESSAGE).replace("{prefix}", Settings.PREFIX));
    }
}