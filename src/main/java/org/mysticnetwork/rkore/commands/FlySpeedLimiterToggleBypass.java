package org.mysticnetwork.rkore.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.util.Tristate;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.UUID;


public class FlySpeedLimiterToggleBypass extends SimpleCommand {

    private final String BYPASS_PERMISSION = Settings.FlySpeedLimiter.BYPASS_PERMISSION;
    private final String BYPASS_TOGGLE_PERMISSION = Settings.FlySpeedLimiter.BYPASS_TOGGLE_PERMISSION;


    public FlySpeedLimiterToggleBypass(SimpleCommandGroup parent) {
        super("flyspeedbypass");
        setDescription(Settings.FlySpeedLimiter.BYPASS_TOGGLE_DESCRIPTION);
        setPermission(BYPASS_PERMISSION);
        setPermissionMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_NO_PERMISSION
                .replace("{prefix}", Settings.PREFIX));
        setAliases(Settings.FlySpeedLimiter.ALIASES);
        setMinArguments(0);
    }

    protected void onCommand() {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_ONLY_PLAYER
                    .replace("{prefix}", Settings.PREFIX));
        }
        Player player = (Player) sender;
        UUID uuid = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getUniqueId();
        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        LuckPerms api = LuckPermsProvider.get();
        assert user != null;
        Tristate bypassPermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_PERMISSION);
        Tristate bypassTogglePermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_TOGGLE_PERMISSION);

        if (bypassPermission.asBoolean()) {
            if (!bypassTogglePermission.asBoolean()) {
                user.data().add(PermissionNode.builder(BYPASS_TOGGLE_PERMISSION).build());
                api.getUserManager().saveUser(user);
                player.sendMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_MESSAGE
                        .replace("{toggle}", Settings.FlySpeedLimiter.BYPASS_TOGGLE_ON)
                        .replace("{prefix}", Settings.PREFIX));
            } else if (bypassTogglePermission.asBoolean()){
                user.data().remove(PermissionNode.builder(BYPASS_TOGGLE_PERMISSION).build());
                api.getUserManager().saveUser(user);
                player.sendMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_MESSAGE
                        .replace("{toggle}", Settings.FlySpeedLimiter.BYPASS_TOGGLE_OFF)
                        .replace("{prefix}", Settings.PREFIX));
            } else {
                player.sendMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_ERROR
                        .replace("{prefix}", Settings.PREFIX));
            }

        } else {
            player.sendMessage(Settings.FlySpeedLimiter.BYPASS_TOGGLE_NO_PERMISSION
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
}

