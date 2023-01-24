package org.mysticnetwork.rkore.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mysticnetwork.rkore.settings.Settings;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;


@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SchemBuilderCommandGroup extends SimpleCommandGroup {

    @Getter(value = AccessLevel.PRIVATE)
    private static final SchemBuilderCommandGroup instance = new SchemBuilderCommandGroup();


    @Override
    protected void registerSubcommands() {
        registerSubcommand(new SchemCommandGive(this));
        registerSubcommand(new FlySpeedLimiterToggleBypass(this));
        registerSubcommand(new ReloadCommand());
    }

    @Override
    protected String getCredits() {return Settings.SchemBuilder.Messages.COMMAND_CREDITS;}

    @Override
    protected String getHeaderPrefix() {
        return Settings.SchemBuilder.Messages.COMMAND_HEADER_PREFIX;
    }

}
