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
public final class RKoreCommandGroup extends SimpleCommandGroup {

    @Getter(value = AccessLevel.PRIVATE)
    private static final RKoreCommandGroup instance = new RKoreCommandGroup();

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
        if (Settings.InfiniteBlocks.ENABLED) {
            registerSubcommand(new InfiniteBlock(this));
        } else {
            RKore.instance.log(1, "[&5RKore&r] &cInfinite Blocks Disabled");
        }
    }

    @Override
    protected String getCredits() {return Settings.SchemBuilder.Messages.COMMAND_CREDITS;}

    @Override
    protected String getHeaderPrefix() {
        return Settings.SchemBuilder.Messages.COMMAND_HEADER_PREFIX;
    }

}
