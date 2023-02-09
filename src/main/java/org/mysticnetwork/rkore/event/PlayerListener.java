package org.mysticnetwork.rkore.event;

import org.mysticnetwork.rkore.model.FactionsUtil;
import org.mysticnetwork.rkore.model.SchematicItem;
import org.mysticnetwork.rkore.model.SchematicPasting;
import org.mysticnetwork.rkore.model.menu.SchemConfirmMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mysticnetwork.rkore.settings.Settings;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();

        SchematicItem schematicItem = SchematicItem.from(item);


        if ( schematicItem == null) return;

        if (SchematicPasting.isPositioning(player)) {
            event.setCancelled(true);
            player.sendMessage((Settings.SchemBuilder.Messages.SCHEMATIC_COMPLETE_PLACEMENT).replace("{prefix}", Settings.PREFIX));
            return;
        }

        if (!FactionsUtil.canSpawnSchematic(player, location, schematicItem.getSchematic())) {
            player.sendMessage((Settings.SchemBuilder.Messages.SCHEMATIC_OVER_LAPPING).replace("{prefix}", Settings.PREFIX));
            event.setCancelled(true);
            return;
        }

        SchematicPasting pasting = schematicItem.startPositioning(player);
        pasting.setSelection(location);

        Block block = event.getBlockPlaced();
        pasting.placeBlock(block);

        new SchemConfirmMenu(pasting, block).displayTo(player);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getMetadata("SchemItem_UUID").isEmpty()) return;

        String uuidString = block.getMetadata("SchemItem_UUID").get(0).asString();

        if (uuidString == null) return;

        SchematicPasting pasting = SchematicPasting.from(UUID.fromString(uuidString));

        if (pasting == null || pasting.getPlayer() != event.getPlayer()) return;

        pasting.deleteSelection(block);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (block.getMetadata("SchemItem_UUID").isEmpty()) return;

        String uuidString = block.getMetadata("SchemItem_UUID").get(0).asString();

        if (uuidString == null) return;

        SchematicPasting pasting = SchematicPasting.from(UUID.fromString(uuidString));
        Player player = event.getPlayer();

        if (pasting == null || pasting.getPlayer() != player || pasting.getHologram() != null) return;

        new SchemConfirmMenu(pasting, block).displayTo(player);
    }
}
