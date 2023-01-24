package org.mysticnetwork.rkore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mysticnetwork.rkore.model.hologram.Hologram;
import org.mysticnetwork.rkore.settings.Settings;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.TimeUtil;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SchematicHologram {
    public SchematicPasting schematicPasting;

    public Hologram hologram;

    public int activation_Time;

    public SchematicHologram(SchematicPasting schematicPasting, Location location) {
        this.schematicPasting = schematicPasting;
        this.hologram = spawnHologram(location.getBlock());
        this.activation_Time = (int) TimeUtil.currentTimeSeconds();
    }

    public static List<Hologram> getHologramInChunk(Chunk chunk) {
        List<SchematicHologram> holograms = Common.convert(SchematicPasting.getSchematics(), SchematicPasting::getHologram);

        if (holograms.isEmpty()) return null;

        List<Hologram> hologramList = new ArrayList<>();
        for (SchematicHologram hologram : holograms) {
            if (hologram.getHologram().getLocation().getChunk().equals(chunk))
                hologramList.add(hologram.getHologram());
        }
        return hologramList;
    }

    public void onTick() {
        updateHologram(hologram);
    }

    public Hologram spawnHologram(Block block) {
        Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
        Hologram hologram = new Hologram(location);
        hologram.spawn(location);
        return hologram;
    }

    public void updateHologram(Hologram hologram) {
        int remainTime = (int) getLeftTime();
        int buildTime = (int) schematicPasting.getSchematic().getBuildTime().getTimeSeconds();
        int percentage = 100 - (remainTime * 100) / buildTime;

        List<String> lore = Settings.SchemBuilder.Hologram.getHologramLore(schematicPasting.getSchematic().getName(), remainTime, percentage);

        hologram.setLore(lore);
        //hologram.drawLore(hologram.getLocation());
        hologram.updateLore(lore);
        if (Settings.SchemBuilder.Hologram.HOLOGRAM_HAS_SOUND) {
            Settings.SchemBuilder.Hologram.HOLOGRAM_PROCESS_SOUND.play(hologram.getLocation());
        }
    }

    public void delete() {
        hologram.remove();
    }

    private long getLeftTime() {
        return schematicPasting.getSchematic().getBuildTime().getTimeSeconds() - getStartTime();
    }

    private long getStartTime() {
        return (TimeUtil.currentTimeSeconds() - activation_Time);
    }


}
