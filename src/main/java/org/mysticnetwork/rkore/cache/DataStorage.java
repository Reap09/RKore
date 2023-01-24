package org.mysticnetwork.rkore.cache;

import lombok.Getter;
import org.mysticnetwork.rkore.model.SchematicItem;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashSet;
import java.util.Set;

@Getter
public class DataStorage extends YamlConfig {

    private static final DataStorage instance = new DataStorage();

    @Getter
    private static Set<SchematicItem> schematicItems = new HashSet<>();


    public void load() {
        loadConfiguration(NO_DEFAULT, "data.db");
    }

    @Override
    protected void onLoad() {
        if (isSet("Schematic_Item")) {
            schematicItems = getSet("Schematic_Item", SchematicItem.class);
        }
    }

    public void addSchematic(final SchematicItem item) {
        Valid.checkNotNull(item, "This item doesn't exists");
        schematicItems.add(item);
        save("Schematic_Item", schematicItems);
    }

    public void removeSchematic(final SchematicItem item) {
        schematicItems.remove(item);
        save("Schematic_Item", schematicItems);
    }

    public static DataStorage getInstance() {
        return instance;
    }
}
