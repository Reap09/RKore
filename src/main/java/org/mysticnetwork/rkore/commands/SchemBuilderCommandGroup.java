package org.mysticnetwork.rkore.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mysticnetwork.rkore.RKore;
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


        if (Settings.SchemBuilder.ENABLED) {
            registerSubcommand(new SchemCommandGive(this));
        }
        registerSubcommand(new ReloadCommand());
        if (Settings.ExternalCommands.ENABLED) {
            registerSubcommand(new ExternalCommand(this));
        } else {
            RKore.instance.log(1, "[&5RKore&r] &cExternal Commands Disabled");
        }
    }

    @Override
    protected String getCredits() {return Settings.SchemBuilder.Messages.COMMAND_CREDITS;}

    @Override
    protected String getHeaderPrefix() {
        return Settings.SchemBuilder.Messages.COMMAND_HEADER_PREFIX;
    }

}
