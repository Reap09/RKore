package org.mysticnetwork.rkore.model;

import lombok.Data;
import lombok.Getter;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.cache.DataStorage;
import org.mysticnetwork.rkore.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.remain.nbt.NBTItem;
import org.mineacademy.fo.visual.VisualizedRegion;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Getter
@Data

public class SchematicPasting {
    private static final ExpiringMap<UUID, SchematicPasting> byName = ExpiringMap.builder().expiration(10, TimeUnit.MINUTES).build();
    private final UUID uuid;
    private final Player player;
    private int rotation;
    SchematicItem schematicItem;
    SchematicHologram hologram;
    private Schematic schematic;
    private boolean withAir;
    private Location selection;
    private VisualizedRegion region;

    public SchematicPasting(Player player, int rotation, SchematicItem schematicItem) {
        this.uuid = UUID.randomUUID();
        this.withAir = schematicItem.getWithAir();

        this.player = player;

        this.rotation = rotation;

        this.schematic = schematicItem.getSchematic();
        this.selection = null;
        this.schematicItem = schematicItem;

        byName.put(uuid, this);
    }

    public static void onTick() {
        for (Player player : Remain.getOnlinePlayers()) {
            SchematicPasting.from(player.getInventory());
        }

        for (SchematicPasting pasting : getSchematics()) {
            if (pasting.hologram != null)
                pasting.hologram.onTick();
            if (pasting.selection != null && !pasting.isBlockPlaced())
                pasting.deleteSelection(pasting.selection.getBlock());
        }
    }

    public void setSelection(Location selection) {
        this.selection = selection;

        if (region != null && region.canSeeParticles(player)) {
            region.hideParticles(player);
        }
        if (selection != null) {
            region = schematic.toRegion(selection, rotation);
            region.showParticles(player);
            region.setParticle(Settings.SchemBuilder.General.SCHEMATIC_CORNER_PARTICLE);
        } else {
            region = null;
        }
    }

    public void confirmSelection(Block block) {
        hologram = new SchematicHologram(this, block.getLocation());

        Common.runLater(getSchematic().getBuildTime().getTimeTicks(), () -> {
            schematic.paste(selection, rotation, withAir);
            block.setType(Material.AIR);

            hologram.delete();

            setSelection(null);
            byName.remove(uuid, this);
            DataStorage.getInstance().removeSchematic(schematicItem);
        });
    }

    public void rotateSelection(int rotation) {
        if (FactionsUtil.canSpawnSchematic(player, rotation, getSelection(), schematic)) {
            setRotation(rotation);
            setSelection(getSelection());
        } else {
            player.sendMessage((Settings.SchemBuilder.Messages.SCHEMATIC_ROTATION_OVER_LAPPING).replace("{prefix}", Settings.PREFIX));
        }
    }

    public void deleteSelection(Block block) {
        setSelection(null);
        destroyBlock(block);

        if (hologram != null)
            hologram.delete();

        byName.remove(uuid, this);
    }

    public void placeBlock(Block block) {
        block.setMetadata("SchemItem_UUID", new FixedMetadataValue(RKore.getInstance(), this.uuid.toString()));
        DataStorage.getInstance().removeSchematic(schematicItem);

        schematicItem = null;
    }

    public void destroyBlock(Block block) {
        if (block == null) return;

        this.schematicItem = new SchematicItem(uuid, schematic, withAir);
        DataStorage.getInstance().addSchematic(schematicItem);

        block.getWorld().dropItemNaturally(block.getLocation(), schematicItem.getSelectionItem());
        block.setType(Material.AIR);
    }

    public static boolean isPositioning(Player player) {
        return from(player) != null;
    }

    public boolean isBlockPlaced() {
        return Settings.SchemBuilder.Schematic_Item.MATERIAL.equals(selection.getBlock());
    }


    public static SchematicPasting from(UUID uuid) {
        for (SchematicPasting schematicPasting : getSchematics()) {
            if (schematicPasting.uuid.equals(uuid)) return schematicPasting;
        }
        return null;
    }

    public static SchematicPasting from(Player player) {
        for (SchematicPasting schematicPasting : getSchematics()) {
            if (schematicPasting.player.equals(player)) return schematicPasting;
        }
        return null;
    }

    public static SchematicPasting from(Inventory inventory) {
        SchematicPasting pasting = null;
        for (ItemStack item : inventory.getContents()) {

            if (item == null) return null;
            if (item.getType() == Material.AIR) return null;


            NBTItem nbtItem = new NBTItem(item);

            if (nbtItem.hasKey("SchemItem_UUID")) {
                String uuid = nbtItem.getString("SchemItem_UUID");

                pasting = SchematicPasting.from(UUID.fromString(uuid));

                if (SchematicItem.from(UUID.fromString(uuid)) == null) {
                    inventory.remove(item);
                }
            }
        }
        return pasting;
    }

    public static Collection<SchematicPasting> getSchematics() {
        return Collections.unmodifiableCollection(byName.values());
    }

}