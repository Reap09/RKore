package org.mysticnetwork.rkore.event;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;
import org.mysticnetwork.rkore.utils.DiscordWebhook;
import org.mysticnetwork.rkore.utils.PlayerHead;

import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.bukkit.Bukkit.*;

public class CommandSpy implements Listener {

    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
    RKore plugin = (RKore) Bukkit.getPluginManager().getPlugin("RKore");
    List<Player> spyingPlayers = plugin.getSpyingPlayers();

    List<String> blackListedCmds = Settings.CommandSpy.InGame.BLACKLISTED_CMDS;

    PlayerHead playerHead = new PlayerHead();



    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission(Settings.CommandSpy.BYPASS_PERMISSION)) {
            String cmd = e.getMessage();
            String[] args = e.getMessage().split(" ");
            if (this.blackListedCmds.contains(args[0]) || (args.length > 1 && this.blackListedCmds.contains(String.valueOf(args[0]) + " " + args[1])))
                return;
            if (!(Settings.CommandSpy.InGame.BLACKLISTED_CMDS.contains(blackListedCmds))) {
                String executor = player.getName();
                ZonedDateTime nowTime = ZonedDateTime.now(ZoneId.of(Settings.CommandSpy.TIMEZONE));
                DateTimeFormatter timeFormatter = null;
                try {
                    timeFormatter = DateTimeFormatter.ofPattern(Settings.CommandSpy.TIMEZONE_FORMAT);
                } catch (DateTimeParseException error) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cTime Format is incorrect!"));
                }
                assert timeFormatter != null;
                String time = nowTime.format(timeFormatter);
                long unixTime = nowTime.toEpochSecond();
                String discordTime = "<t:" + unixTime + ">";
                String discordRTime = "<t:" + unixTime + ":R>";
                UserManager lp = LuckPermsProvider.get().getUserManager();
                User user = lp.getUser(executor);
                String lpGroup = user.getPrimaryGroup();
                CachedDataManager userData = user.getCachedData();
                String lpPrefix = userData.getMetaData().getPrefix();
                if (lpPrefix == null) {
                    lpPrefix = "no-prefix";
                }
                if (lpGroup == null) {
                    lpGroup = "no-group";
                }
                String translatedPrefix = ColorUtils.translateColorCodes(lpPrefix);
                String serverName = player.getServer().getServerName();
                String playerName = e.getPlayer().getName();

                List<String> messages = Settings.CommandSpy.Discord.Embed.MESSAGES;
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).isEmpty()) {
                        messages.set(i, "\\n");
                    }
                }
                String message = String.join("\\n", messages);
                int titleLength = Settings.CommandSpy.Discord.Embed.TITLE.length();
                int fieldsCount = messages.size();
                int footerLength = Settings.CommandSpy.Discord.Embed.FOOTER
                        .replace("{player}", executor)
                        .replace("{cmd}", cmd)
                        .replace("{time}", discordTime)
                        .replace("{server}", serverName)
                        .replace("{lp-group}", lpGroup)
                        .replace("{lp-prefix}", translatedPrefix).length();
                if (titleLength > 256) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cTitle length exceeded the limit of 256 characters. Current length is: " + titleLength));
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cPlease adjust the config file to fix this issue."));
                    return;
                }
                if (fieldsCount > 25) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cFields count exceeded the limit of 25. Current count is: " + fieldsCount));
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cPlease adjust the config file to fix this issue."));
                    return;
                }
                if (footerLength > 2048) {
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cFooter length exceeded the limit of 2048 characters. Current length is: " + footerLength));
                    console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cPlease adjust the config file to fix this issue."));
                    return;
                }
                if (Settings.CommandSpy.InGame.ENABLED) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (spyingPlayers.contains(p)) {
                            try {
                                p.sendMessage(Settings.CommandSpy.InGame.MESSAGE
                                        .replace("{player}", executor)
                                        .replace("{cmd}", cmd)
                                        .replace("{time}", time)
                                        .replace("{lp-group}", lpGroup)
                                        .replace("{lp-prefix}", translatedPrefix));

                            } catch (NullPointerException error) {
                                System.out.println(error);
                            }
                        }
                    }
                }
                if (Settings.CommandSpy.Discord.ENABLED) {
                    DiscordWebhook webhook = new DiscordWebhook(Settings.CommandSpy.Discord.WEBHOOK_URL);
                    webhook.setAvatarUrl(Settings.CommandSpy.Discord.WEBHOOK_AVATAR
                            .replace("{player}", executor)
                            .replace("{cmd}", cmd)
                            .replace("{server}", serverName)
                            .replace("{time}", discordTime)
                            .replace("{r-time}", discordRTime)
                            .replace("{lp-group}", lpGroup)
                            .replace("{lp-prefix}", translatedPrefix));
                    webhook.setUsername(Settings.CommandSpy.Discord.WEBHOOK_USERNAME
                            .replace("{player}", executor)
                            .replace("{cmd}", cmd)
                            .replace("{server}", serverName)
                            .replace("{time}", discordTime)
                            .replace("{r-time}", discordRTime)
                            .replace("{lp-group}", lpGroup)
                            .replace("{lp-prefix}", translatedPrefix));
                    if (!(Settings.CommandSpy.Discord.Embed.ENABLED)) {
                        webhook.setContent(Settings.CommandSpy.Discord.MESSAGE
                                .replace("{player}", executor)
                                .replace("{cmd}", cmd)
                                .replace("{server}", serverName)
                                .replace("{time}", discordTime)
                                .replace("{r-time}", discordRTime)
                                .replace("{lp-group}", lpGroup)
                                .replace("{lp-prefix}", translatedPrefix));
                    } else {
                        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                                .setColor(Color.decode(Settings.CommandSpy.Discord.Embed.COLOR))
                                .setThumbnail(Settings.CommandSpy.Discord.Embed.THUMBNAIL.replace("{player-head}", playerHead.getPlayerHeadUrl(playerName)))
                                .addField("", message
                                        .replace("{player}", executor)
                                        .replace("{cmd}", cmd)
                                        .replace("{server}", serverName)
                                        .replace("{time}", discordTime)
                                        .replace("{r-time}", discordRTime)
                                        .replace("{lp-group}", lpGroup)
                                        .replace("{lp-prefix}", translatedPrefix), false)
                                .setTitle(Settings.CommandSpy.Discord.Embed.TITLE
                                        .replace("{player}", executor)
                                        .replace("{cmd}", cmd)
                                        .replace("{server}", serverName)
                                        .replace("{time}", discordTime)
                                        .replace("{r-time}", discordRTime)
                                        .replace("{lp-group}", lpGroup)
                                        .replace("{lp-prefix}", translatedPrefix))
                                .setFooter(Settings.CommandSpy.Discord.Embed.FOOTER
                                        .replace("{player}", executor)
                                        .replace("{cmd}", cmd)
                                        .replace("{time}", discordTime)
                                        .replace("{r-time}", discordRTime)
                                        .replace("{server}", serverName)
                                        .replace("{lp-group}", lpGroup)
                                        .replace("{lp-prefix}", translatedPrefix), ""));
                    } try {
                        webhook.execute();
                    } catch (java.io.IOException ioException) {
                        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cCouldn't find a valid discord webhook url in settings.yml for command spy at: &4" + Settings.CommandSpy.Discord.WEBHOOK_URL));
                    } catch (NullPointerException nullPointerException) {
                        System.out.println(nullPointerException);
                    }
                }
            }
        }
    }
}
