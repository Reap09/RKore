package org.mysticnetwork.rkore.settings;

import org.bukkit.Server;
import org.mysticnetwork.rkore.model.Schematic;
import org.mysticnetwork.rkore.model.SchematicPasting;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.util.List;
import java.util.Set;

public class Settings extends SimpleSettings {
    protected int getConfigVersion() {
        return 1;
    }

    public static String PREFIX;
    public static Integer LICENSE_KEY;

    private static void init() {
        PREFIX = ColorUtils.translateColorCodes(Settings.getString("prefix"));
        LICENSE_KEY = Settings.getInteger("license-key");
    }

    public static class ClearLag {

        public static Boolean ENABLED;
        public static String DESCRIPTION;
        public static String NO_PERMISSION_MESSAGE;
        public static String PERMISSION;
        public static String CLEARED_MESSAGE;
        public static Boolean AUTO_INTERVAL;
        public static Integer INTERVAL;
        public static String INTERVAL_FORMAT;
        public static List<String> INTERVAL_MESSAGES;
        public static List<String> MESSAGE_TIMINGS;
        public static List<String> COMMANDS_ON_CLEAR;
        public static List<String> ALLOWED_ENTITIES;

        private static void init() {
            Settings.setPathPrefix("clear-lag");
            ENABLED = Settings.getBoolean("enabled");
            DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            NO_PERMISSION_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("no-permission-message"));
            PERMISSION = Settings.getString("permission");
            CLEARED_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("cleared-message"));
            AUTO_INTERVAL = Settings.getBoolean("auto-interval");
            INTERVAL = Settings.getInteger("interval");
            INTERVAL_FORMAT = Settings.getString("interval-format");
            INTERVAL_MESSAGES = Settings.getStringList("interval-messages");
            MESSAGE_TIMINGS = Settings.getStringList("message-timings");
            COMMANDS_ON_CLEAR = Settings.getStringList("commands-on-clear");
            ALLOWED_ENTITIES = Settings.getStringList("allowed-entities");
        }
    }

    public static class FlySpeedLimiter {

        public static Boolean ENABLED;
        public static String NO_PERMISSION_MESSAGE;
        public static String BYPASS_PERMISSION;
        public static Double MAX_HORIZONTAL_FLY_SPEED;
        public static String BYPASS_TOGGLE_DESCRIPTION;
        public static String BYPASS_TOGGLE_PERMISSION;
        public static String BYPASS_TOGGLE_NO_PERMISSION;
        public static String BYPASS_TOGGLE_ONLY_PLAYER;
        public static String BYPASS_TOGGLE_MESSAGE;
        public static String BYPASS_TOGGLE_ERROR;
        public static String BYPASS_TOGGLE_ON;
        public static String BYPASS_TOGGLE_OFF;
        public static List<String> KICK_MESSAGE;

        private static void init() {
            Settings.setPathPrefix("fly-speed-limiter");
            ENABLED = Settings.getBoolean("enabled");
            NO_PERMISSION_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("no-permission-message"));
            BYPASS_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("bypass-permission"));
            MAX_HORIZONTAL_FLY_SPEED = Settings.getDouble("max-horizontal");
            //MAX_VERTICAL_FLY_SPEED = Settings.getDouble("max-vertical");
            KICK_MESSAGE = Settings.getStringList("kick-message");
            Settings.setPathPrefix("fly-speed-limiter.bypass-toggle");
            BYPASS_TOGGLE_DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            BYPASS_TOGGLE_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("message"));
            BYPASS_TOGGLE_ERROR = ColorUtils.translateColorCodes(Settings.getString("toggle-error"));
            BYPASS_TOGGLE_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("permission"));
            BYPASS_TOGGLE_NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
            BYPASS_TOGGLE_ONLY_PLAYER = ColorUtils.translateColorCodes(Settings.getString("only-player"));
            BYPASS_TOGGLE_ON = ColorUtils.translateColorCodes(Settings.getString("on-placeholder"));
            BYPASS_TOGGLE_OFF = ColorUtils.translateColorCodes(Settings.getString("off-placeholder"));
        }
    }

    public static class SchemBuilder {
        public static class General {
            public static String SCHEMATIC_PERMISSION;
            public static SimpleTime SCHEMATIC_ITEM_TIME;
            public static CompParticle SCHEMATIC_CORNER_PARTICLE;

            private static void init() {
                Settings.setPathPrefix("schembuilder.general");
                SCHEMATIC_PERMISSION = Settings.getString("permission");
                SCHEMATIC_ITEM_TIME = Settings.getTime("schem-item-time");
                SCHEMATIC_CORNER_PARTICLE = CompParticle.valueOf(Settings.getString("schem-corner-particle"));
            }
        }

        public static class Hologram {
            public static Boolean HOLOGRAM_HAS_SOUND;
            public static SimpleSound HOLOGRAM_PROCESS_SOUND;
            public static List<String> HOLOGRAM_LORE;

            private static void init() {
                Settings.setPathPrefix("schembuilder.hologram");
                HOLOGRAM_HAS_SOUND = Boolean.valueOf(Settings.getBoolean("has-sound"));
                HOLOGRAM_PROCESS_SOUND = Settings.getSound("sound");
                HOLOGRAM_LORE = Settings.getStringList("hologram");
            }

            public static List<String> getHologramLore(String name, int remainTime, int percentage) {
                return Replacer.replaceVariables(HOLOGRAM_LORE, SerializedMap.ofArray(new Object[]{"{name", name, "{time}",

                        SimpleTime.fromSeconds(remainTime).toString(), "{percentage}", percentage + "%"}));
            }
        }

        public static class Messages {
            public static String COMMAND_CREDITS;
            public static String COMMAND_HEADER_PREFIX;
            public static String COMMAND_GIVE_DESCRIPTION;
            public static String COMMAND_GIVE_NO_PLAYER_FOUND;
            public static String COMMAND_GIVE_NO_SCHEMATIC_FOUND;
            public static String COMMAND_GIVE_SUCCESSFUL;
            public static String MUST_BE_BOOLEAN;
            public static String SCHEMATIC_COMPLETE_PLACEMENT;
            public static String SCHEMATIC_OVER_LAPPING;
            public static String SCHEMATIC_ROTATION_OVER_LAPPING;

            private static void init() {
                Settings.setPathPrefix("schembuilder.messages.schematic");
                SCHEMATIC_COMPLETE_PLACEMENT = ColorUtils.translateColorCodes(Settings.getString("complete-placement"));
                SCHEMATIC_OVER_LAPPING = ColorUtils.translateColorCodes(Settings.getString("over-lapping"));
                SCHEMATIC_ROTATION_OVER_LAPPING = ColorUtils.translateColorCodes(Settings.getString("rotation-over-lapping"));
                Settings.setPathPrefix("schembuilder.messages.commands");
                COMMAND_HEADER_PREFIX = ColorUtils.translateColorCodes(Settings.getString("header-prefix"));
                COMMAND_CREDITS = ColorUtils.translateColorCodes("Plugin Created by Reap_9 and SpiderDeluxe");
                COMMAND_GIVE_DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("give.description"));
                COMMAND_GIVE_NO_PLAYER_FOUND = ColorUtils.translateColorCodes(Settings.getString("give.no-player-found"));
                COMMAND_GIVE_NO_SCHEMATIC_FOUND = ColorUtils.translateColorCodes(Settings.getString("give.no-schematic-found"));
                MUST_BE_BOOLEAN = ColorUtils.translateColorCodes(Settings.getString("give.must-be-boolean"));
                COMMAND_GIVE_SUCCESSFUL = ColorUtils.translateColorCodes(Settings.getString("give.successful"));
            }
        }

        public static class Schematic_Item {
            public static CompMaterial MATERIAL;

            public static String DISPLAY_NAME;

            public static List<String> DESCRIPTION;

            private static void init() {
                Settings.setPathPrefix("schembuilder.schematic-item");
                MATERIAL = Settings.getMaterial("material");
                DISPLAY_NAME = ColorUtils.translateColorCodes(Settings.getString("display-name"));
                DESCRIPTION = Settings.getStringList("lore");
            }

            public static ItemStack getSchematicItem(Schematic schematic) {
                return Settings.SchemBuilder.toItem(MATERIAL, DISPLAY_NAME, DESCRIPTION, schematic).make();
            }
        }

        public static class Schematic_Menu {
            public static String TITLE;

            public static CompMaterial ACCEPT_BUTTON_MATERIAL;

            public static String ACCEPT_BUTTON_TITLE;

            public static List<String> ACCEPT_BUTTON_DESCRIPTION;

            public static CompMaterial REFUSE_BUTTON_MATERIAL;

            public static String REFUSE_BUTTON_TITLE;

            public static List<String> REFUSE_BUTTON_DESCRIPTION;

            public static CompMaterial ROTATE_BUTTON_MATERIAL;

            public static String ROTATE_BUTTON_TITLE;

            public static List<String> ROTATE_BUTTON_DESCRIPTION;

            private static void init() {
                Settings.setPathPrefix("schembuilder.schematic-gui");
                TITLE = ColorUtils.translateColorCodes(Settings.getString("title"));
                Settings.setPathPrefix("schembuilder.schematic-gui.accept-button");
                ACCEPT_BUTTON_MATERIAL = Settings.getMaterial("material");
                ACCEPT_BUTTON_TITLE = ColorUtils.translateColorCodes(Settings.getString("title"));
                ACCEPT_BUTTON_DESCRIPTION = Settings.getStringList("lore");
                Settings.setPathPrefix("schembuilder.schematic-gui.refuse-button");
                REFUSE_BUTTON_MATERIAL = Settings.getMaterial("material");
                REFUSE_BUTTON_TITLE = ColorUtils.translateColorCodes(Settings.getString("title"));
                REFUSE_BUTTON_DESCRIPTION = Settings.getStringList("lore");
                Settings.setPathPrefix("schembuilder.schematic-gui.rotate-button");
                ROTATE_BUTTON_MATERIAL = Settings.getMaterial("material");
                ROTATE_BUTTON_TITLE = ColorUtils.translateColorCodes(Settings.getString("title"));
                ROTATE_BUTTON_DESCRIPTION = Settings.getStringList("lore");
            }

            public static ItemCreator getAcceptButton(SchematicPasting schematic) {
                return Settings.SchemBuilder.toItem(ACCEPT_BUTTON_MATERIAL, ACCEPT_BUTTON_TITLE, ACCEPT_BUTTON_DESCRIPTION, schematic);
            }

            public static ItemCreator getRefuseButton(SchematicPasting schematic) {
                return Settings.SchemBuilder.toItem(REFUSE_BUTTON_MATERIAL, REFUSE_BUTTON_TITLE, REFUSE_BUTTON_DESCRIPTION, schematic);
            }

            public static ItemCreator getRotateButton(SchematicPasting schematic) {
                return Settings.SchemBuilder.toItem(ROTATE_BUTTON_MATERIAL, ROTATE_BUTTON_TITLE, ROTATE_BUTTON_DESCRIPTION, schematic);
            }
        }

        public static ItemCreator toItem(CompMaterial material, String title, List<String> description, Schematic schematic) {
            return ItemCreator.of(material, title, getDescription(description, schematic));
        }

        public static ItemCreator toItem(CompMaterial material, String title, List<String> description, SchematicPasting schematicPasting) {
            return ItemCreator.of(material, title, getDescription(description, schematicPasting));
        }

        public static List<String> getDescription(List<String> description, SchematicPasting schematicPasting) {
            Schematic schematic = schematicPasting.getSchematic();
            return Replacer.replaceVariables(description, SerializedMap.ofArray(new Object[]{"{name}", (schematic == null) ? "None" : schematic
                    .getName(), "{level}", (schematic == null) ? "None" : schematic
                    .getLevel(), "{rotation}", (schematic == null) ? "None" :
                    Integer.valueOf(schematicPasting.getRotation()), "{time}", (schematic == null) ? "None" : schematic
                    .getBuildTime().toString()}));
        }

        public static List<String> getDescription(List<String> description, Schematic schematic) {
            return Replacer.replaceVariables(description, SerializedMap.ofArray(new Object[]{"{name}", (schematic == null) ? "None" : schematic
                    .getName(), "{level}", (schematic == null) ? "None" : schematic
                    .getLevel(), "{time}", (schematic == null) ? "None" : schematic
                    .getBuildTime().toString()}));
        }


    }
}