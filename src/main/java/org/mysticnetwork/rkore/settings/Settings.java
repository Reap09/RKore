package org.mysticnetwork.rkore.settings;

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

public class Settings extends SimpleSettings {


    protected int getConfigVersion() {
        return 1;
    }

    public static String PREFIX;
    public static String LICENSE_KEY;

    private static void init() {
        PREFIX = ColorUtils.translateColorCodes(Settings.getString("prefix"));
        LICENSE_KEY = Settings.getString("license-key");
    }

    public static class ClearInventory {
        public static Boolean ENABLED;
        public static List<String> ALIASES;
        public static String DESCRIPTION;
        public static String PERMISSION;
        public static String BYPASS_PERMISSION;
        public static String NO_PERMISSION;
        public static String ONLY_PLAYER;
        public static String INVALID_PLAYER;
        public static String INVALID_ITEMS;
        public static String CLEAR_SELF;
        public static String CLEAR_TARGET;
        public static String CLEARED_TARGET;
        public static String INVALID_AMOUNT;
        public static String CLEAR_ITEM_SELF;
        public static String CLEAR_ITEM_TARGET;
        public static String REMOVED_ITEM;
        public static String CONFIRM_MESSAGE;
        public static String CONFIRM_TIME;
        private static void init() {
            Settings.setPathPrefix("clear-inventory");
            ENABLED = Settings.getBoolean("enabled");
            ALIASES = Settings.getStringList("aliases");
            DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            PERMISSION = ColorUtils.translateColorCodes(Settings.getString("permission"));
            BYPASS_PERMISSION = Settings.getString("bypass-permission");
            NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
            ONLY_PLAYER = ColorUtils.translateColorCodes(Settings.getString("only-player"));
            INVALID_PLAYER = ColorUtils.translateColorCodes(Settings.getString("invalid-player"));
            INVALID_ITEMS = ColorUtils.translateColorCodes(Settings.getString("invalid-items"));
            CLEAR_SELF = ColorUtils.translateColorCodes(Settings.getString("clear-self"));
            CLEAR_TARGET = ColorUtils.translateColorCodes(Settings.getString("clear-target"));
            CLEARED_TARGET = ColorUtils.translateColorCodes(Settings.getString("cleared-target"));
            INVALID_AMOUNT = ColorUtils.translateColorCodes(Settings.getString("invalid-amount"));
            CLEAR_ITEM_SELF = ColorUtils.translateColorCodes(Settings.getString("clear-item-self"));
            CLEAR_ITEM_TARGET = ColorUtils.translateColorCodes(Settings.getString("clear-item-target"));
            REMOVED_ITEM = ColorUtils.translateColorCodes(Settings.getString("removed-item"));
            CONFIRM_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("confirm-message"));
            CONFIRM_TIME = Settings.getString("confirm-time");
        }
    }

    public static class CommandSpy {
        public static Boolean ENABLED;
        public static String BYPASS_PERMISSION;
        public static String TIMEZONE;
        public static String TIMEZONE_FORMAT;

        private static void init() {
            Settings.setPathPrefix("command-spy");
            ENABLED = Settings.getBoolean("enabled");
            BYPASS_PERMISSION = Settings.getString("bypass-permission");
            TIMEZONE = Settings.getString("timezone");
            TIMEZONE_FORMAT = Settings.getString("timezone-format");
        }

        public static class InGame {
            public static Boolean ENABLED;
            public static List<String> ALIASES;
            public static String DESCRIPTION;
            public static String PERMISSION;
            public static String NO_PERMISSION;
            public static String ONLY_PLAYER;
            public static String TOGGLE_MESSAGE;
            public static String ON_PLACEHOLDER;
            public static String OFF_PLACEHOLDER;
            public static String MESSAGE;
            public static List<String> BLACKLISTED_CMDS;

            private static void init() {
                Settings.setPathPrefix("command-spy.in-game");
                ENABLED = Settings.getBoolean("enabled");
                ALIASES = Settings.getStringList("aliases");
                DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
                PERMISSION = ColorUtils.translateColorCodes(Settings.getString("permission"));
                NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
                ONLY_PLAYER = ColorUtils.translateColorCodes(Settings.getString("only-player"));
                TOGGLE_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("toggle-message"));
                ON_PLACEHOLDER = ColorUtils.translateColorCodes(Settings.getString("on-placeholder"));
                OFF_PLACEHOLDER = ColorUtils.translateColorCodes(Settings.getString("off-placeholder"));
                MESSAGE = ColorUtils.translateColorCodes(Settings.getString("message"));
                BLACKLISTED_CMDS = Settings.getStringList("blacklisted-cmds");
            }
        }

        public static class Discord {
            public static Boolean ENABLED;
            public static String WEBHOOK_URL;
            public static String WEBHOOK_AVATAR;
            public static String WEBHOOK_USERNAME;
            public static String MESSAGE;

            private static void init() {
                Settings.setPathPrefix("command-spy.discord");
                ENABLED = Settings.getBoolean("enabled");
                WEBHOOK_URL = Settings.getString("webhook-url");
                WEBHOOK_AVATAR = Settings.getString("webhook-avatar");
                WEBHOOK_USERNAME = Settings.getString("webhook-username");
                MESSAGE = Settings.getString("message");
            }

            public static class Embed {
                public static Boolean ENABLED;
                public static String TITLE;
                public static String COLOR;
                public static String THUMBNAIL;
                public static String FOOTER;
                public static List<String> MESSAGES;

                private static void init() {
                    Settings.setPathPrefix("command-spy.discord.embed");
                    ENABLED = Settings.getBoolean("enabled");
                    TITLE = Settings.getString("title");
                    COLOR = Settings.getString("color");
                    THUMBNAIL = Settings.getString("thumbnail");
                    FOOTER = Settings.getString("footer");
                    MESSAGES = Settings.getStringList("messages");
                }
            }
        }
    }

    public static class InfoCommands {
        public static Boolean ENABLED;

        public static Boolean DISCORD_ENABLED;
        public static List<String> DISCORD_ALIASES;
        public static String DISCORD_PERMISSION;
        public static String DISCORD_MESSAGE;
        public static String DISCORD_DESCRIPTION;
        public static String DISCORD_NO_PERMISSION;

        public static Boolean WEBSITE_ENABLED;
        public static List<String> WEBSITE_ALIASES;
        public static String WEBSITE_PERMISSION;
        public static String WEBSITE_MESSAGE;
        public static String WEBSITE_DESCRIPTION;
        public static String WEBSITE_NO_PERMISSION;

        public static Boolean STORE_ENABLED;
        public static List<String> STORE_ALIASES;
        public static String STORE_PERMISSION;
        public static String STORE_MESSAGE;
        public static String STORE_DESCRIPTION;
        public static String STORE_NO_PERMISSION;

        private static void init() {
            Settings.setPathPrefix("info-commands");
            ENABLED = Settings.getBoolean("enabled");
            Settings.setPathPrefix("info-commands.discord");
            DISCORD_ENABLED = Settings.getBoolean("enabled");
            DISCORD_ALIASES = Settings.getStringList("aliases");
            DISCORD_PERMISSION = Settings.getString("permission");
            DISCORD_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("message"));
            DISCORD_DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            DISCORD_NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
            Settings.setPathPrefix("info-commands.website");
            WEBSITE_ENABLED = Settings.getBoolean("enabled");
            WEBSITE_ALIASES = Settings.getStringList("aliases");
            WEBSITE_PERMISSION = Settings.getString("permission");
            WEBSITE_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("message"));
            WEBSITE_DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            WEBSITE_NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
            Settings.setPathPrefix("info-commands.store");
            STORE_ENABLED = Settings.getBoolean("enabled");
            STORE_ALIASES = Settings.getStringList("aliases");
            STORE_PERMISSION = Settings.getString("permission");
            STORE_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("message"));
            STORE_DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            STORE_NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("no-permission"));
        }
    }

    public static class ClearLag {

        public static Boolean ENABLED;
        public static List<String> ALIASES;
        public static String DESCRIPTION;
        public static String NO_PERMISSION_MESSAGE;
        public static String PERMISSION;
        public static String TIME_PERMISSION;
        public static String TIME_NO_PERMISSION;
        public static String TIME_MESSAGE;
        public static String NO_TIME;
        public static String ONLY_PLAYER;
        public static String CLEARED_MESSAGE;
        public static Boolean AUTO_INTERVAL;
        public static Integer INTERVAL;
        public static String INTERVAL_FORMAT;
        public static List<String> INTERVAL_MESSAGES;
        public static List<String> COMMANDS_ON_CLEAR;
        public static List<String> ALLOWED_ENTITIES;

        private static void init() {
            Settings.setPathPrefix("clear-lag");
            ENABLED = Settings.getBoolean("enabled");
            ALIASES = Settings.getStringList("aliases");
            DESCRIPTION = ColorUtils.translateColorCodes(Settings.getString("description"));
            NO_PERMISSION_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("no-permission-message"));
            PERMISSION = Settings.getString("permission");
            TIME_PERMISSION = Settings.getString("time-permission");
            TIME_NO_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("time-no-permission-message"));
            TIME_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("time-message"));
            NO_TIME = ColorUtils.translateColorCodes(Settings.getString("no-time"));
            ONLY_PLAYER = ColorUtils.translateColorCodes(Settings.getString("only-player"));
            CLEARED_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("cleared-message"));
            AUTO_INTERVAL = Settings.getBoolean("auto-interval");
            INTERVAL = Settings.getInteger("interval");
            INTERVAL_FORMAT = Settings.getString("interval-format");
            INTERVAL_MESSAGES = Settings.getStringList("interval-messages");
            COMMANDS_ON_CLEAR = Settings.getStringList("commands-on-clear");
            ALLOWED_ENTITIES = Settings.getStringList("allowed-entities");
        }
    }

    public static class FlySpeedLimiter {

        public static Boolean ENABLED;
        public static List<String> ALIASES;
        public static String NO_PERMISSION_MESSAGE;
        public static String BYPASS_PERMISSION;
        public static Boolean DEBUG_HORIZONTAL;
        public static Boolean DEBUG_VERTICAL;
        public static Double MAX_HORIZONTAL_FLY_SPEED;
        public static Double MAX_VERTICAL_FLY_SPEED;
        public static Boolean SETBACK;

        public static Boolean BYPASS_TOGGLE_ENABLED;
        public static String BYPASS_TOGGLE_DESCRIPTION;
        public static String BYPASS_TOGGLE_PERMISSION;
        public static String BYPASS_TOGGLE_NO_PERMISSION;
        public static String BYPASS_TOGGLE_ONLY_PLAYER;
        public static String BYPASS_TOGGLE_MESSAGE;
        public static String BYPASS_TOGGLE_ERROR;
        public static String BYPASS_TOGGLE_ON;
        public static String BYPASS_TOGGLE_OFF;
        public static Boolean KICK_ENABLED;
        public static List<String> KICK_MESSAGE;

        private static void init() {
            Settings.setPathPrefix("fly-speed-limiter");
            ENABLED = Settings.getBoolean("enabled");
            ALIASES = Settings.getStringList("aliases");
            NO_PERMISSION_MESSAGE = ColorUtils.translateColorCodes(Settings.getString("no-permission-message"));
            BYPASS_PERMISSION = ColorUtils.translateColorCodes(Settings.getString("bypass-permission"));
            DEBUG_HORIZONTAL = Settings.getBoolean("debug.horizontal");
            DEBUG_VERTICAL = Settings.getBoolean("debug.vertical");
            MAX_HORIZONTAL_FLY_SPEED = Settings.getDouble("max-horizontal");
            MAX_VERTICAL_FLY_SPEED = Settings.getDouble("max-vertical");
            SETBACK = Settings.getBoolean("setback");
            KICK_ENABLED = Settings.getBoolean("kick-enabled");
            KICK_MESSAGE = Settings.getStringList("kick-message");
            Settings.setPathPrefix("fly-speed-limiter.bypass-toggle");
            BYPASS_TOGGLE_ENABLED = Settings.getBoolean("enabled");
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

        public static Boolean ENABLED;

        private static void init() {
            Settings.setPathPrefix("schembuilder");
            ENABLED = Settings.getBoolean("enabled");
        }

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
            public static String COMMAND_NO_PERMISSION;
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
                COMMAND_NO_PERMISSION = ColorUtils.translateColorCodes("give.no-permission");
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