package org.mysticnetwork.rkore.model;

import lombok.Data;
import lombok.SneakyThrows;
import org.mysticnetwork.rkore.cache.DataStorage;
import org.mysticnetwork.rkore.settings.Settings;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.nbt.NBTItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
public class SchematicItem implements ConfigSerializable {
    private UUID uuid;
    private Schematic schematic;

    private OfflinePlayer player;

    private long leftTime;
    private Boolean withAir;


    public SchematicItem(UUID uuid, Schematic schematic, boolean withAir) {
        this.uuid = uuid;
        this.schematic = schematic;
        this.withAir = withAir;
        this.leftTime = TimeUtil.currentTimeSeconds() + Settings.SchemBuilder.General.SCHEMATIC_ITEM_TIME.getTimeSeconds();
    }

    public SchematicItem(UUID uuid, Schematic schematic, long leftTime, boolean withAir) {
        this.uuid = uuid;
        this.schematic = schematic;
        this.withAir = withAir;
        this.leftTime = leftTime;
    }

    public SchematicPasting startPositioning(Player player) {
        return new SchematicPasting(player, Schematic.convertPlayerGrade(player), this);
    }

    public void give(Player player) {
        PlayerUtil.addItemsOrDrop(player, getSelectionItem());
    }

    public ItemStack getSelectionItem() {
        ItemStack item = ItemCreator.of(Settings.SchemBuilder.Schematic_Item.getSchematicItem(schematic)).make();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("SchemItem_UUID", uuid.toString());
        nbtItem.setString("SchemItem_Structure", uuid.toString());
        nbtItem.applyNBT(item);
        return item;
    }

    public boolean isValid() {
        return TimeUtil.currentTimeSeconds() < leftTime;
    }

    public void deleteIfOutTime() {
        if (!isValid()) DataStorage.getInstance().removeSchematic(this);
    }

    public static SchematicItem from(UUID uuid) {
        for (SchematicItem schematicItem : DataStorage.getSchematicItems()) {
            if (schematicItem.uuid.equals(uuid)) return schematicItem;
        }
        return null;
    }

    public static SchematicItem from(ItemStack item) {
        SchematicItem schematicItem = null;

        if (item == null) return null;
        if (item.getType() == Material.AIR) return null;


        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasKey("SchemItem_UUID")) {
            String uuid = nbtItem.getString("SchemItem_UUID");

            schematicItem = SchematicItem.from(UUID.fromString(uuid));

        }
        return schematicItem;
    }

    @Override
    public SerializedMap serialize() {
        return SerializedMap.ofArray(
                "UUID", uuid,
                "Schematic", schematic.getName(),
                "Left_Time", TimeUtil.getFormattedDate(leftTime * 1000),
                "withAir", withAir);
    }

    @SneakyThrows
    public static SchematicItem deserialize(SerializedMap map) {
        final UUID uuid = map.getUUID("UUID");
        Schematic schematic = Schematic.findSchematic(map.getString("Schematic"));

        long leftTime = convertTime(map.getString("Left_Time")) / 1000;
        boolean withAir = map.getBoolean("withAir");

        return new SchematicItem(uuid, schematic, leftTime, withAir);
    }

    public static Long convertTime(final String time) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        final Date date = format.parse(time);
        return date.getTime();
    }
}
