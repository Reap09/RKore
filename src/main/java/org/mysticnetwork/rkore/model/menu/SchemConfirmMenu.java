package org.mysticnetwork.rkore.model.menu;

import org.mysticnetwork.rkore.model.SchematicPasting;
import org.mysticnetwork.rkore.settings.Settings;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;

public class SchemConfirmMenu extends Menu {

    SchematicPasting pasting;

    Button acceptButton;
    Button denyButton;

    Button rotateButton;

    public SchemConfirmMenu(SchematicPasting pasting, Block block) {
        super();
        setSize(9 * 3);
        this.pasting = pasting;

        setTitle(Settings.SchemBuilder.Schematic_Menu.TITLE);

        acceptButton = Button.makeSimple(Settings.SchemBuilder.Schematic_Menu.getAcceptButton(pasting), player -> {
            pasting.confirmSelection(block);
            player.closeInventory();
        });
        denyButton = Button.makeSimple(Settings.SchemBuilder.Schematic_Menu.getRefuseButton(pasting), player -> {
            pasting.deleteSelection(block);
            player.closeInventory();
        });

        rotateButton = Button.makeSimple(Settings.SchemBuilder.Schematic_Menu.getRotateButton(pasting), player -> {
            int value = pasting.getRotation();
            int nextRotation = value == 360 ? 0 : value + 90;

            pasting.rotateSelection(nextRotation);
            player.closeInventory();
        });
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot== 9 * 1 + 4)
            return rotateButton.getItem();
        if (slot == 9 * 1 + 2)
            return acceptButton.getItem();
        if (slot == 9 * 1 + 6)
            return denyButton.getItem();

        return super.getItemAt(slot);
    }


}
