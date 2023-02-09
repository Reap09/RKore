package org.mysticnetwork.rkore.event;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class FlySpeedLimiter implements Listener {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
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
            if (!bypassTogglePermission.asBoolean() || !bypassPermission.asBoolean()) {
                double xDiff = e.getTo().getX() - e.getFrom().getX();
                double zDiff = e.getTo().getZ() - e.getFrom().getZ();
                double horizontalSpeed = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                horizontalSpeed = horizontalSpeed * 20;
                double yDiff = e.getTo().getY() - e.getFrom().getY();
                double verticalSpeed = Math.abs(yDiff);
                verticalSpeed = verticalSpeed * 20;
                if (Settings.FlySpeedLimiter.DEBUG_VERTICAL) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] [&bVertical&r] " + verticalSpeed));
                }
                if (Settings.FlySpeedLimiter.DEBUG_HORIZONTAL) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] [&eHorizontal&r] " + horizontalSpeed));
                }
                Location newLocation = e.getFrom();
                if (horizontalSpeed > MAX_HORIZONTAL_FLY_SPEED && !Settings.FlySpeedLimiter.DEBUG_HORIZONTAL) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        if (Settings.FlySpeedLimiter.SETBACK) {
                            newLocation.setX(e.getFrom().getX());
                            newLocation.setZ(e.getFrom().getZ());
                            player.teleport(newLocation);
                            e.getPlayer().sendMessage(Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                        }
                    }
                }
                if (verticalSpeed > MAX_VERTICAL_FLY_SPEED && !Settings.FlySpeedLimiter.DEBUG_VERTICAL) {
                    if (Settings.FlySpeedLimiter.KICK_ENABLED) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FlySpeedLimiter.KICK_MESSAGE)));
                    } else {
                        if (Settings.FlySpeedLimiter.SETBACK) {
                            newLocation.setY(e.getFrom().getY());
                            player.teleport(newLocation);
                            e.getPlayer().sendMessage(Settings.FlySpeedLimiter.NO_PERMISSION_MESSAGE.replace("{prefix}", Settings.PREFIX));
                        }
                    }
                }
            }
        }
    }
}

