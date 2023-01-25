package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.List;


public class ClearLag extends SimpleSubCommand {


    public ClearLag(SimpleCommandGroup parent) {
        super(parent, "clearlag");
        setDescription(Settings.FlySpeedLimiter.BYPASS_TOGGLE_DESCRIPTION);
        setPermission(Settings.ClearLag.PERMISSION);
        setMinArguments(0);
    }
    private int removedEntities = 0;

    protected void onCommand() {
        Player player = (Player) sender;
        removedEntities = 0;
        List<String> commandList = Settings.ClearLag.COMMANDS_ON_CLEAR;
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player) && !(entity instanceof ArmorStand) && !Settings.ClearLag.ALLOWED_ENTITIES.contains(entity.getType().name())) {
                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        Material material = item.getItemStack().getType();
                        if (!Settings.ClearLag.ALLOWED_ENTITIES.contains(material.name())) {
                            entity.remove();
                            removedEntities++;
                        }
                    } else {
                        entity.remove();
                        removedEntities++;
                    }
                }
            }
        }
        for (String commandString : commandList){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);

        }
        String totalRemovedEntities = String.valueOf((Integer) removedEntities);
        player.sendMessage(Settings.ClearLag.CLEARED_MESSAGE.replace("{prefix}", Settings.PREFIX).replace("{amount}", totalRemovedEntities));
    }
}

