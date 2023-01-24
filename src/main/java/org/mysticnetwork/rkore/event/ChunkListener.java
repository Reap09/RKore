package org.mysticnetwork.rkore.event;

import org.mysticnetwork.rkore.model.SchematicHologram;
import org.mysticnetwork.rkore.model.hologram.Hologram;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.List;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();

        List<Hologram> holograms = SchematicHologram.getHologramInChunk(chunk);
        if (holograms != null)
        Hologram.temporaryDeSpawn(holograms);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        List<Hologram> holograms = SchematicHologram.getHologramInChunk(chunk);
        if (holograms != null)
            Hologram.temporaryDeSpawn(holograms);
    }
}
