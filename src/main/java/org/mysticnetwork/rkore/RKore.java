package org.mysticnetwork.rkore;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mysticnetwork.rkore.cache.DataStorage;
import org.mysticnetwork.rkore.commands.Discord;
import org.mysticnetwork.rkore.commands.Store;
import org.mysticnetwork.rkore.commands.Website;
import org.mysticnetwork.rkore.event.ChunkListener;
import org.mysticnetwork.rkore.event.FlySpeedLimiter;
import org.mysticnetwork.rkore.event.PlayerListener;
import org.mysticnetwork.rkore.licensedb.Database;
import org.mysticnetwork.rkore.model.Schematic;
import org.mysticnetwork.rkore.model.SchematicItem;
import org.mysticnetwork.rkore.model.SchematicPasting;
import org.mysticnetwork.rkore.model.hologram.Hologram;
import org.mysticnetwork.rkore.runnable.AutoClearLag;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

public final class RKore extends SimplePlugin {
    public static RKore instance;
    public Database database;
    public static RKore getInstance() {
        return instance;
    }
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();

    public void onPluginStart() {



        instance = this;
//        if (Settings.LICENSE_KEY == 0) {
//            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cPlease enter a valid license key in settings.yml"));
//            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
//            setEnabled(false);
//            return;
//        }
        console.sendMessage(ColorUtils.translateColorCodes("[&dRKore licensing&r] Current license key in config.yml:&d " + Settings.LICENSE_KEY));

        this.database = new Database();

        try {
//            this.database.initializeDatabase();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &4Could not initialize license database."));
//            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
//            setEnabled(false);
//            return;

        } catch (Error e) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);
            return;
        }

        if (Package.getPackage("org.mysticnetwork.rkore.cache") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Cache"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);
            return;
        }
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Cache"));

        if (Package.getPackage("org.mysticnetwork.rkore.commands") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Commands"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);
            return;
        }
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Commands"));

        if (Package.getPackage("org.mysticnetwork.rkore.event") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Event"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);
            return;
        }
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Events"));

        if (Package.getPackage("org.mysticnetwork.rkore.model") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Model"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);
            return;
        }
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Models"));

        if (Package.getPackage("org.mysticnetwork.rkore.settings") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Settings"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);

        }
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Settings"));

        if (Package.getPackage("org.mysticnetwork.rkore.runnable") == null) {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cNo package found of Runnable"));
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabling Plugin."));
            setEnabled(false);

        } else {
            console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &aSuccessfully loaded Runnable"));
        }
    }

    public void onReloadablesStart() {
        instance = this;
        if (Settings.ClearLag.AUTO_INTERVAL) {
            if (Settings.ClearLag.INTERVAL_FORMAT.equalsIgnoreCase("seconds")) {
                getInstance().getServer().getScheduler().runTaskTimer(instance, new AutoClearLag(), 0, 20*Settings.ClearLag.INTERVAL);
            } else if (Settings.ClearLag.INTERVAL_FORMAT.equalsIgnoreCase("minutes")) {
                getInstance().getServer().getScheduler().runTaskTimer(instance, new AutoClearLag(), 0, 1200*Settings.ClearLag.INTERVAL);
            } else if (Settings.ClearLag.INTERVAL_FORMAT.equalsIgnoreCase("hours")) {
                getInstance().getServer().getScheduler().runTaskTimer(instance, new AutoClearLag(), 0, 72000*Settings.ClearLag.INTERVAL);
            } else {
                console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cUnable to retrieve &einterval-format: '"+ Settings.ClearLag.INTERVAL_FORMAT+"' 7c from settings.yml"));
            }
        }
        if (Settings.InfoCommands.DISCORD_ENABLED) {
            registerCommand(new Discord(getMainCommand()));
        }
        if (Settings.InfoCommands.WEBSITE_ENABLED) {
            registerCommand(new Website(getMainCommand()));
        }
        if (Settings.InfoCommands.STORE_ENABLED) {
            registerCommand(new Store(getMainCommand()));
        }
        registerEvents(new FlySpeedLimiter());
        registerEvents(new PlayerListener());
        registerEvents(new ChunkListener());
        Schematic.loadAll();
        DataStorage.getInstance().load();
        Common.runTimer(20, () -> {
            Set<SchematicItem> schematicItemSet = new HashSet<>(DataStorage.getSchematicItems());
            for (SchematicItem item : schematicItemSet)
                item.deleteIfOutTime();
            SchematicPasting.onTick();
        });
        Hologram.deleteAll();
    }

    public void onPluginStop() {
        console.sendMessage(ColorUtils.translateColorCodes("[&5RKore&r] &cDisabled"));
    }

}