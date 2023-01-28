package org.mysticnetwork.rkore.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.mysticnetwork.rkore.settings.Settings;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mysticnetwork.rkore.utils.ColorUtils;

import static org.bukkit.Bukkit.getServer;


@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SchemBuilderCommandGroup extends SimpleCommandGroup {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();

    @Getter(value = AccessLevel.PRIVATE)
    private static final SchemBuilderCommandGroup instance = new SchemBuilderCommandGroup();

    @Override
    protected void registerSubcommands() {
        if (Settings.FlySpeedLimiter.ENABLED) {
            registerSubcommand(new FlySpeedLimiterToggleBypass(this));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aFly Speed Limiter Toggle Enabled"));
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cFly Speed Limiter Toggle Disabled"));
        }


        if (Settings.ClearLag.ENABLED) {
            registerSubcommand(new ClearLag(this));
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cClear lag Command Disabled"));
        }


        if (Settings.SchemBuilder.ENABLED) {
            registerSubcommand(new SchemCommandGive(this));
        }
        registerSubcommand(new ReloadCommand());
    }

    @Override
    protected String getCredits() {return Settings.SchemBuilder.Messages.COMMAND_CREDITS;}

    @Override
    protected String getHeaderPrefix() {
        return Settings.SchemBuilder.Messages.COMMAND_HEADER_PREFIX;
    }

}
