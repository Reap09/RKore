package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.mysticnetwork.rkore.model.Schematic;
import org.mysticnetwork.rkore.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;
import java.util.Objects;

public class SchemCommandGive extends SimpleSubCommand {
    public SchemCommandGive(SimpleCommandGroup parent) {
        super(parent, "give");
        setDescription(Settings.SchemBuilder.Messages.COMMAND_GIVE_DESCRIPTION);
        setPermission(Settings.SchemBuilder.General.SCHEMATIC_PERMISSION);
        setPermissionMessage(Settings.SchemBuilder.Messages.COMMAND_NO_PERMISSION);
        setUsage("<player> <name> [with_air]");
        setMinArguments(2);
    }


    protected void onCommand() {
        Player player = (Player) sender;
        String argPlayer = args[0];
        Player stringPlayer = Bukkit.getPlayer(argPlayer);

        if (stringPlayer == null) {
            player.sendMessage((Settings.SchemBuilder.Messages.COMMAND_GIVE_NO_PLAYER_FOUND).replace("{player}", this.args[0]).replace("{prefix}", Settings.PREFIX));

        } else {

            String param = this.args[1];
            Schematic schematic = Schematic.findSchematic(param);
            try{
            if (schematic == null) {
                player.sendMessage((Settings.SchemBuilder.Messages.COMMAND_GIVE_NO_SCHEMATIC_FOUND.replace("{name}", param).replace("{prefix}", Settings.PREFIX)));
                return;
            }
            } catch (NullPointerException e) {
                return;
            }
            boolean withAir = true;
            if (this.args.length > 2) {
                if (!(Objects.equals(this.args[2], "true") || Objects.equals(this.args[2], "false"))) {
                    player.sendMessage((Settings.SchemBuilder.Messages.MUST_BE_BOOLEAN).replace("{prefix}", Settings.PREFIX));
                    return;
                }
                withAir = Boolean.parseBoolean(this.args[2]);
            }
            schematic.giveItem(player, withAir);
            player.sendMessage((Settings.SchemBuilder.Messages.COMMAND_GIVE_SUCCESSFUL.replace("{name}", schematic.getName()).replace("{prefix}", Settings.PREFIX)));
        }
    }

    protected List<String> tabComplete() {
        if (this.args.length == 1)
            return completeLastWordPlayerNames();
        if (this.args.length == 2)
            return completeLastWord(Schematic.getSchematicDisplayNames());
        return completeLastWord((Object[])new String[] { "true", "false" });
    }
}
