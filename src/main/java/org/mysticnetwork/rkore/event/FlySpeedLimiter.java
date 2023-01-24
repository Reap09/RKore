package org.mysticnetwork.rkore.event;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.util.UUID;

public class FlySpeedLimiter implements Listener {
    private final double MAX_HORIZONTAL_FLY_SPEED = Settings.FlySpeedLimiter.MAX_HORIZONTAL_FLY_SPEED;
    private final String BYPASS_PERMISSION = Settings.FlySpeedLimiter.BYPASS_PERMISSION;
    private final String BYPASS_TOGGLE_PERMISSION = Settings.FlySpeedLimiter.BYPASS_TOGGLE_PERMISSION;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
            Player player = e.getPlayer();
        UUID playerUUID = e.getPlayer().getUniqueId();
        User user = LuckPermsProvider.get().getUserManager().getUser(playerUUID);
        Tristate bypassPermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_PERMISSION);
        Tristate bypassTogglePermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_TOGGLE_PERMISSION);

        if (!bypassPermission.asBoolean()) {
            double currentSpeed = e.getFrom().distance(e.getTo());
//
            if (currentSpeed > MAX_HORIZONTAL_FLY_SPEED) {
                e.setCancelled(true);
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n",Settings.FlySpeedLimiter.KICK_MESSAGE)));
                e.getPlayer().sendMessage(" VERTICAL " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
            }
        } else if (!bypassTogglePermission.asBoolean()) {
            double currentSpeed = e.getFrom().distance(e.getTo());
            if (currentSpeed > MAX_HORIZONTAL_FLY_SPEED) {
                e.setCancelled(true);
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n",Settings.FlySpeedLimiter.KICK_MESSAGE)));
                e.getPlayer().sendMessage(" VERTICAL " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
            }
        }
    }
}

