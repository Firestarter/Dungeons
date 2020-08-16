package com.firestartermc.dungeons.lobby.gui;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.firestartermc.dungeons.shared.DungeonInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.firestartermc.kerosene.gui.Gui;
import com.firestartermc.kerosene.gui.GuiPosition;
import com.firestartermc.kerosene.gui.components.buttons.ButtonComponent;
import com.firestartermc.kerosene.gui.components.cosmetic.BorderComponent;
import com.firestartermc.kerosene.util.item.ItemBuilder;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public class DungeonSelectorGui extends Gui {
    
    private static DungeonSelectorGui instance;
    
    public static void open(Player player) {
        if (instance == null) {
            instance = new DungeonSelectorGui();
        }
        
        instance.open(new Player[] { player });
    }

    public static void updateGui() {
        if (instance != null) {
            instance.update(); // TODO update items
        }
    }

    private DungeonSelectorGui() {
        super("Select a dungeon", 3);

        addElement(new BorderComponent());

        AtomicInteger slot = new AtomicInteger(10);
        DungeonsLobby.getDungeonManager().getDungeons().stream()
                .sorted(Comparator.comparingInt(DungeonInfo::getDisplayOrder))
                .forEach(dungeon -> {
            addElement(getButton(slot.getAndIncrement(), dungeon));
        });
    }

    private ButtonComponent getButton(int slot, DungeonInfo dungeonInfo) {
        Material material = Material.getMaterial(dungeonInfo.getMaterial());
        if (material == null) {
            material = Material.IRON_DOOR;
        }

        return new ButtonComponent(
                GuiPosition.fromSlot(slot),
                new ItemBuilder(material)
                        .name(dungeonInfo.getName())
                        .build(),
                event -> {
                    event.getPlayer().sendMessage("Selected: " + dungeonInfo.getName() + " (" + dungeonInfo.getId() + ")");
                }
        );
    }
}
