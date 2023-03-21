package org.mysticnetwork.rkore;

import java.sql.Timestamp;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mysticnetwork.rkore.cache.DataStorage;
import org.mysticnetwork.rkore.commands.*;
import org.mysticnetwork.rkore.event.*;
import org.mysticnetwork.rkore.licensedb.Licensing;
import org.mysticnetwork.rkore.model.Schematic;
import org.mysticnetwork.rkore.model.SchematicItem;
import org.mysticnetwork.rkore.model.SchematicPasting;
import org.mysticnetwork.rkore.model.hologram.Hologram;
import org.mysticnetwork.rkore.runnable.AutoClearLag;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import static org.mysticnetwork.rkore.model.Schematic.getSchematics;

public final class RKore extends SimplePlugin {
    public static RKore instance;


    public static RKore getInstance() {
        return instance;
    }

    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();

    private List<Player> spyingPlayers = new ArrayList<Player>();

    public List<Player> getSpyingPlayers() {
        return spyingPlayers;
    }

    private Map<UUID, Timestamp> confirmations = new HashMap<>();
    public Map<UUID, Timestamp> getConfirmations() {
        return confirmations;
    }
    private Licensing.LogType logType = Licensing.LogType.NORMAL;
    public RKore setConsoleLog(Licensing.LogType logType) {
        this.logType = logType;
        return this;
    }


    public void onPluginStart() {
        instance = this;

        log(1,"[&5RKore&r] Schematics Loaded: &5" + getSchematics().size());

        if (Package.getPackage("org.mysticnetwork.rkore.cache") == null) {
            log(1,"[&5RKore&r] &cNo package found of Cache");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
            return;
        }
        log(1,"[&5RKore&r] &aSuccessfully loaded Cache");

        if (Package.getPackage("org.mysticnetwork.rkore.commands") == null) {
            log(1,"[&5RKore&r] &cNo package found of Commands");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
            return;
        }
        log(1,"[&5RKore&r] &aSuccessfully loaded Commands");

        if (Package.getPackage("org.mysticnetwork.rkore.event") == null) {
            log(1,"[&5RKore&r] &cNo package found of Event");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
            return;
        }
        log(1,"[&5RKore&r] &aSuccessfully loaded Events");

        if (Package.getPackage("org.mysticnetwork.rkore.model") == null) {
            log(1,"[&5RKore&r] &cNo package found of Model");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
            return;
        }
        log(1,"[&5RKore&r] &aSuccessfully loaded Models");

        if (Package.getPackage("org.mysticnetwork.rkore.settings") == null) {
            log(1,"[&5RKore&r] &cNo package found of Settings");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
            return;
        }
        log(1,"[&5RKore&r] &aSuccessfully loaded Settings");

        if (Package.getPackage("org.mysticnetwork.rkore.runnable") == null) {
            log(1,"[&5RKore&r] &cNo package found of Runnable");
            log(1,"[&5RKore&r] &cDisabling Plugin.");
            setEnabled(false);
        } else {
            log(1,"[&5RKore&r] &aSuccessfully loaded Runnable");
        }


        // Event Registration

        if (Settings.SchemBuilder.ENABLED) {
            registerEvents(new PlayerListener());
            registerEvents(new ChunkListener());
            log(1,"[&5RKore&r] &aSchem Builder Enabled");
        } else {
            log(1,"[&5RKore&r] &aSchem Builder Disabled");
        }

        if (Settings.FlySpeedLimiter.ENABLED) {
            registerEvents(new FlySpeedLimiter());
            log(1,"[&5RKore&r] &aFly Speed Limiter Enabled");
        } else {
            log(1,"[&5RKore&r] &cFly Speed Limiter Disabled");
        }

        if (Settings.CommandSpy.ENABLED) {
            registerEvents(new CommandSpy());
            log(1,"[&5RKore&r] &aCommand Spy Enabled");
        } else {
            log(1,"[&5RKore&r] &cCommand Spy Disabled");
        }


        // Runnable Registration
        if (Settings.ClearLag.ENABLED) {
            if (Settings.ClearLag.AUTO_INTERVAL) {
                getInstance().getServer().getScheduler().runTaskTimer(instance, new AutoClearLag(), 20 * 5, 20);
                log(1,"[&5RKore&r] &aClear Lag Auto Interval Enabled");
            } else {
                log(1,"[&5RKore&r] &cClear Lag Auto Interval Disabled");
            }
        } else {
            log(1,"[&5RKore&r] &cClear Lag Disabled");
        }


        // Command Registration

        if (Settings.CommandSpy.InGame.ENABLED) {
            registerCommand(new CmdSpy(getMainCommand()));
        }

        if (Settings.InfoCommands.ENABLED) {
            if (Settings.InfoCommands.DISCORD_ENABLED) {
                registerCommand(new Discord(getMainCommand()));
            }
            if (Settings.InfoCommands.WEBSITE_ENABLED) {
                registerCommand(new Website(getMainCommand()));
            }
            if (Settings.InfoCommands.STORE_ENABLED) {
                registerCommand(new Store(getMainCommand()));
            }
        } else {
            log(1,"[&5RKore&r] &cInfo Commands Disabled");
        }

        if (Settings.ClearInventory.ENABLED) {
            registerCommand(new ClearInventory(getMainCommand()));
        } else {
            log(1,"[&5RKore&r] &cClear Inventory Disabled");
        }

        if (Settings.FlySpeedLimiter.ENABLED) {
            registerCommand(new FlySpeedLimiterToggleBypass(getMainCommand()));
        } else {
            log(1,"[&5RKore&r] &cFly Speed Limiter Toggle Disabled");
        }


        if (Settings.ClearLag.ENABLED) {
            registerCommand(new ClearLag(getMainCommand()));
        } else {
            log(1,"[&5RKore&r] &cClear lag Command Disabled");
        }
    }

    public void onReloadablesStart() {

        instance = this;

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
        log(1,"[&5RKore&r] &cDisabled");
    }
    public void log(int type, String message) {
        if (logType == Licensing.LogType.NONE || (logType == Licensing.LogType.LOW && type == 0))
            return;
        console.sendMessage(ColorUtils.translateColorCodes(message));
    }
    Boolean papiEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");


}