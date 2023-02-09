package org.mysticnetwork.rkore.model;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.mysticnetwork.rkore.cache.DataStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.YamlConfig;
import org.mineacademy.fo.visual.VisualizedRegion;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.io.File;
import java.util.*;

import static org.bukkit.Bukkit.getServer;


@Getter
public class Schematic extends YamlConfig {

    private static final Map<String, Schematic> byName = new HashMap<>();


    private final String id_name;

    private final Clipboard clipboard;

    private String name;

    private SimpleTime buildTime;

    private String level;

    public Schematic(File file) {
        this.id_name = file.getName().replace(".schematic", "").replace(".schem", "");
        this.clipboard = WorldEditHook.loadClipboard(file);

        this.setPathPrefix("Schematics." + id_name);
        this.loadConfiguration(NO_DEFAULT, "schematic.yml");

        byName.put(id_name, this);
    }


    @Override
    protected void onLoad() {
        name = getString("Display_Name", id_name);
        buildTime = getTime("Build_Time", SimpleTime.fromSeconds(60));
        level = getString("Level", "I");

        save();
    }

    @Override
    protected void onSave() {
        set("Display_Name", name);
        set("Build_Time", buildTime);
        set("Level", level);
    }

    public static Schematic findSchematic(String name) {
        return byName.values().stream()
                .filter(schematic -> schematic.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
     static Server server = getServer();
    static ConsoleCommandSender console = server.getConsoleSender();
    public static void loadAll() {
        byName.clear();
        for (final File file : FileUtil.getFiles("Schematic", "schematic")) {
            new Schematic(file);
        };
    }

    public void paste(Location location, int rotation, boolean withAir) {
        WorldEditHook.pasteClipboard(clipboard, location, rotation, withAir);
    }

    public void giveItem(Player player, boolean withAir) {
        SchematicItem schematicItem = new SchematicItem(UUID.randomUUID(), this, withAir);
        schematicItem.give(player);
        DataStorage.getInstance().addSchematic(schematicItem);
    }

    public VisualizedRegion toRegion(Location location, Player player) {
        return toRegion(location, Schematic.convertPlayerGrade(player));
    }

    public VisualizedRegion toRegion(Location originLoc, int rotation) {
        return WorldEditHook.getClipboardRegion(clipboard, originLoc, rotation);
    }

    public static int convertPlayerGrade(Player player) {
        int yaw = (int) player.getEyeLocation().getYaw();

        if ((yaw <= -45 && yaw > -135) || (yaw >= 45 && yaw < 135)) {
            return 90;
        } else if ((yaw <= -135 && yaw > -225) || (yaw >= 135 && yaw < 225)) {
            return 180;
        } else if ((yaw <= -225 && yaw > -315) || (yaw >= 225 && yaw < 315))
            return 270;
        else
            return 0;
    }

    public static Collection<Schematic> getSchematics() {
        return Collections.unmodifiableCollection(byName.values());
    }

    public static Collection<String> getSchematicDisplayNames() {
        return Collections.unmodifiableCollection(Common.convert(getSchematics(), schematic -> schematic.name));
    }
}