package org.mysticnetwork.rkore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mysticnetwork.rkore.RKore;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;


public class InfiniteBlock extends SimpleSubCommand {

        public InfiniteBlock(SimpleCommandGroup parent) {
            super(parent, "infiniteblock");
            setDescription(Settings.InfiniteBlocks.DESCRIPTION);
            setPermission(null);
            setPermissionMessage(Settings.InfiniteBlocks.NO_PERMISSION
                    .replace("{prefix}", Settings.PREFIX));
            setUsage("<player> <infiniteblock>");
            setAliases(Settings.InfiniteBlocks.ALIASES);
            setMinArguments(2);
        }


    protected void onCommand() {
        if (sender.hasPermission(Settings.InfiniteBlocks.PERMISSION) || Settings.InfiniteBlocks.PERMISSION.equals("none")) {
            //Player player = (Player) sender;

            String targetPlayerName = args[0];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer == null) {
                sender.sendMessage(Settings.InfiniteBlocks.NO_PLAYER
                        .replace("{prefix}", Settings.PREFIX)
                        .replace("{target}", args[0]));
                return;
            }

            String infiniteBlockName = args[1];
            ConfigurationSection infiniteBlockSection = RKore.instance.getInfiniteBlocksConfig().getConfigurationSection("infinite-blocks." + infiniteBlockName);

            if (infiniteBlockSection == null) {
                sender.sendMessage(Settings.InfiniteBlocks.NO_ITEM
                        .replace("{prefix}", Settings.PREFIX)
                        .replace("{item}", args[1]));
                return;
            }

            Material material = Material.valueOf(infiniteBlockSection.getString("type"));
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (infiniteBlockSection.getBoolean("glow")) {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            }

            String displayName = ColorUtils.translateColorCodes(infiniteBlockSection.getString("display-name", infiniteBlockName));
            meta.setDisplayName(displayName);

            if (infiniteBlockSection.isList("lore")) {
                List<String> lore = infiniteBlockSection.getStringList("lore");
                List<String> coloredLore = new ArrayList<>();

                for (String line : lore) {
                    coloredLore.add(ColorUtils.translateColorCodes(line));
                }
                StringBuilder hiddenBlockName = new StringBuilder();
                for (char c : args[1].toCharArray()) {
                    hiddenBlockName.append(ChatColor.COLOR_CHAR).append(c);
                }
                if (coloredLore.size() > 0) {
                    String lastLine = coloredLore.get(coloredLore.size() - 1);
                    coloredLore.set(coloredLore.size() - 1, lastLine + hiddenBlockName.toString());
                } else {
                    coloredLore.add(hiddenBlockName.toString());
                }
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
            targetPlayer.getInventory().addItem(item);
            sender.sendMessage(Settings.InfiniteBlocks.SUCCESS_GIVE
                    .replace("{prefix}", Settings.PREFIX)
                    .replace("{player}", sender.getName())
                    .replace("{target}", targetPlayerName)
                    .replace("{item}", infiniteBlockName));
            targetPlayer.sendMessage(Settings.InfiniteBlocks.SUCCESS_GIVEN
                    .replace("{prefix}", Settings.PREFIX)
                    .replace("{player}", sender.getName())
                    .replace("{target}", targetPlayerName)
                    .replace("{item}", infiniteBlockName));
        } else {
            sender.sendMessage(Settings.InfiniteBlocks.NO_PERMISSION
                    .replace("{prefix}", Settings.PREFIX));
        }
    }
    protected List<String> tabComplete() {
        if (this.args.length == 1)
            return completeLastWordPlayerNames();
        if (this.args.length == 2)
            return completeLastWord(RKore.instance.getInfiniteBlocksConfig().getConfigurationSection("infinite-blocks").getKeys(false));
        return null;
    }
}
