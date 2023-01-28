package org.mysticnetwork.rkore.event;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.UUID;

public class FlySpeedLimiter implements Listener {
    private final Double MAX_HORIZONTAL_FLY_SPEED = Settings.FlySpeedLimiter.MAX_HORIZONTAL_FLY_SPEED;
    private final Double MAX_VERTICAL_FLY_SPEED = Settings.FlySpeedLimiter.MAX_VERTICAL_FLY_SPEED;
    private final String BYPASS_PERMISSION = Settings.FlySpeedLimiter.BYPASS_PERMISSION;
    private final String BYPASS_TOGGLE_PERMISSION = Settings.FlySpeedLimiter.BYPASS_TOGGLE_PERMISSION;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = e.getPlayer().getUniqueId();
        User user = LuckPermsProvider.get().getUserManager().getUser(playerUUID);
        assert user != null;
        Tristate bypassPermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_PERMISSION);
        Tristate bypassTogglePermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_TOGGLE_PERMISSION);
        if (player.isFlying()) {
            if (!bypassPermission.asBoolean()) {
                double xDiff = e.getTo().getX() - e.getFrom().getX();
                double zDiff = e.getTo().getX() - e.getFrom().getX();
                double horizontalSpeed = Math.sqrt(xDiff * xDiff * zDiff * zDiff);
                double yDiff = e.getTo().getY() - e.getFrom().getY();
                double verticalSpeed = Math.abs(yDiff);
                if (horizontalSpeed > MAX_HORIZONTAL_FLY_SPEED) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        Location newLocation = player.getLocation();
                        newLocation.setX(e.getFrom().getX() + (MAX_HORIZONTAL_FLY_SPEED * (e.getTo().getX() - e.getFrom().getX())));
                        newLocation.setZ(e.getFrom().getZ() + (MAX_HORIZONTAL_FLY_SPEED * (e.getTo().getZ() - e.getFrom().getZ())));
                        player.teleport(newLocation);
                        e.getPlayer().sendMessage(" HORIZONTAL " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                    }
                }
                if (verticalSpeed > MAX_VERTICAL_FLY_SPEED) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        Location newLocation = player.getLocation();
                        newLocation.setY(e.getFrom().getY() + (MAX_VERTICAL_FLY_SPEED * (e.getTo().getY() - e.getFrom().getY())));
                        player.teleport(newLocation);
                        e.getPlayer().sendMessage(" VERTICAL " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                    }
                }
            }
            if (!bypassTogglePermission.asBoolean()) {
                double xDiff = e.getTo().getX() - e.getFrom().getX();
                double zDiff = e.getTo().getX() - e.getFrom().getX();
                double horizontalSpeed = Math.sqrt(xDiff * xDiff * zDiff * zDiff);
                double yDiff = e.getTo().getY() - e.getFrom().getY();
                double verticalSpeed = Math.abs(yDiff);
                if (horizontalSpeed > MAX_HORIZONTAL_FLY_SPEED) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        Location newLocation = player.getLocation();
                        newLocation.setX(e.getFrom().getX() + (MAX_HORIZONTAL_FLY_SPEED * (e.getTo().getX() - e.getFrom().getX())));
                        newLocation.setZ(e.getFrom().getZ() + (MAX_HORIZONTAL_FLY_SPEED * (e.getTo().getZ() - e.getFrom().getZ())));
                        player.teleport(newLocation);
                        e.getPlayer().sendMessage(" Horizontal " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                    }
                }
                if (verticalSpeed > MAX_VERTICAL_FLY_SPEED) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        Location newLocation = player.getLocation();
                        newLocation.setY(e.getFrom().getY() + (MAX_VERTICAL_FLY_SPEED * (e.getTo().getY() - e.getFrom().getY())));
                        player.teleport(newLocation);
                        e.getPlayer().sendMessage(" Vertical " + Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                    }
                }
            }
        }
    }
}

