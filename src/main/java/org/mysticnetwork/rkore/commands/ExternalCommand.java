package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ExternalCommand extends SimpleSubCommand {
    private static final String BR_ARG_PREFIX = "br:";
    private static final String UUID_ARG_PREFIX = "uuid:";

    public ExternalCommand(SimpleCommandGroup parent) {
        super(parent, "external");
        setDescription(Settings.ExternalCommands.DESCRIPTION);
        setPermission(null);
        setPermissionMessage(Settings.ExternalCommands.NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.ExternalCommands.ALIASES);
        setUsage("<cmd>");
    }

    protected void onCommand() {
        if (sender instanceof Player) {
            if (!(sender.hasPermission(Settings.ExternalCommands.PERMISSION) || Settings.ExternalCommands.PERMISSION.equals("null"))) {
                sender.sendMessage(Settings.ExternalCommands.NO_PERMISSION
                        .replace("{prefix}", Settings.PREFIX));
                return;
            }
        }


        String externalCmd = String.join(" ", args);

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith(BR_ARG_PREFIX)) {
                String playerName = args[i].substring(3).replace(Settings.ExternalCommands.BEDROCK_PREFIX, "");
                externalCmd = externalCmd.replace(args[i], playerName);
            } else if (args[i].startsWith(UUID_ARG_PREFIX)) {
                String playerName = args[i].substring(UUID_ARG_PREFIX.length());
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer == null) {
                    sender.sendMessage(Settings.ExternalCommands.NO_PLAYER
                            .replace("{prefix}", Settings.PREFIX)
                            .replace("{player}", playerName));
                    return;
                }
                UUID playerUUID = targetPlayer.getUniqueId();
                externalCmd = externalCmd.replace(args[i], playerUUID.toString());
            }
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), externalCmd);
        if (Settings.ExternalCommands.ENABLED) {
            sender.sendMessage(Settings.ExternalCommands.SUCCESS_MESSAGE
                    .replace("{prefix}", Settings.PREFIX)
                    .replace("{cmd}", externalCmd));
        }
    }

    protected List<String> tabComplete() {
        String lastArg = args[args.length - 1];
        for (String arg : args) {
            if (arg.contains(BR_ARG_PREFIX)) {
                String partialName = lastArg;
                if (lastArg.startsWith(BR_ARG_PREFIX)) {
                    partialName = lastArg.substring(BR_ARG_PREFIX.length());
                }

                List<String> playerNames = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith(partialName)) {
                        playerNames.add(BR_ARG_PREFIX + player.getName());
                    }
                }

                return playerNames;
            } else if (arg.contains(UUID_ARG_PREFIX)) {
                String partialName = lastArg;
                if (lastArg.startsWith(UUID_ARG_PREFIX)) {
                    partialName = lastArg.substring(UUID_ARG_PREFIX.length());
                }

                List<String> playerNames = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith(partialName)) {
                        playerNames.add(UUID_ARG_PREFIX + player.getName());
                    }
                }

                return playerNames;
            }
        }
        return null;
    }
}
