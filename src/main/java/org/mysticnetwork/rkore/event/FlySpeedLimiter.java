package org.mysticnetwork.rkore.event;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.mysticnetwork.rkore.RKore;
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
    private final String BEDROCK_PLAYER = Settings.FlySpeedLimiter.BEDROCK_PLAYER;
    private boolean justEnabledFly = false;


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = e.getPlayer().getUniqueId();
        User user = LuckPermsProvider.get().getUserManager().getUser(playerUUID);
        assert user != null;
        Tristate bypassPermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_PERMISSION);
        Tristate bypassTogglePermission = user.getCachedData().getPermissionData().checkPermission(BYPASS_TOGGLE_PERMISSION);
        Tristate bedrockPlayer = user.getCachedData().getPermissionData().checkPermission(BEDROCK_PLAYER);
        if (player.isFlying()) {
            Location playerLeft = player.getLocation().clone().add(.2, 0, 0);
            Location playerRight = player.getLocation().clone().subtract(.2, 0, 0);
            Location playerForward = player.getLocation().clone().add(0, 0, .1);
            Location playerBackward = player.getLocation().clone().subtract(0, 0, .1);
            Location playerHead = player.getLocation().clone().add(0, .01, 0);
            Location playerFeet = player.getLocation().clone().subtract(0, .01, 0);
            if (!(e.getTo().getBlock().getType() == Material.WATER || e.getTo().getBlock().getType() == Material.STATIONARY_WATER ||
                    playerHead.getBlock().getType() == Material.WATER || playerHead.getBlock().getType() == Material.STATIONARY_WATER ||
                    playerFeet.getBlock().getType() == Material.WATER || playerFeet.getBlock().getType() == Material.STATIONARY_WATER ||
                    playerLeft.getBlock().getType() == Material.WATER || playerLeft.getBlock().getType() == Material.STATIONARY_WATER ||
                    playerRight.getBlock().getType() == Material.WATER || playerRight.getBlock().getType() == Material.STATIONARY_WATER ||
                    playerForward.getBlock().getType() == Material.WATER || playerForward.getBlock().getType() == Material.STATIONARY_WATER ||
                    playerBackward.getBlock().getType() == Material.WATER || playerBackward.getBlock().getType() == Material.STATIONARY_WATER)) {
                if (!bypassTogglePermission.asBoolean() || !bypassPermission.asBoolean()) {
                    if (!bedrockPlayer.asBoolean()) {
                        if (justEnabledFly) {
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
        }
    }

    @EventHandler
    public void onPlayerToggleFly(PlayerToggleFlightEvent e) {
        Bukkit.getScheduler().runTaskLater(RKore.instance, () -> {
            justEnabledFly = e.isFlying();
        }, 3);
    }
}

