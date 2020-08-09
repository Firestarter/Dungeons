package com.firestartermc.dungeons.lobby.gui;

import com.firestartermc.dungeons.common.dungeon.DungeonInfo;
import com.firestartermc.dungeons.lobby.DungeonsLobby;
import org.bukkit.entity.Player;
import xyz.nkomarn.kerosene.gui.Gui;
import xyz.nkomarn.kerosene.gui.GuiPosition;
import xyz.nkomarn.kerosene.gui.components.buttons.ButtonComponent;
import xyz.nkomarn.kerosene.gui.components.cosmetic.BorderComponent;
import xyz.nkomarn.kerosene.util.item.ItemBuilder;

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
                .sorted(Comparator.comparingInt(DungeonInfo::getOrder))
                .forEach(dungeon -> {
            addElement(getButton(slot.getAndIncrement(), dungeon));
        });
    }

    @Override
    protected void render(Player player) {
        super.render(player);
    }

    private ButtonComponent getButton(int slot, DungeonInfo dungeonInfo) {
        return new ButtonComponent(
                GuiPosition.fromSlot(slot),
                new ItemBuilder(dungeonInfo.getMaterial())
                        .name(dungeonInfo.getName())
                        .build(),
                event -> {
                    event.getPlayer().sendMessage("Selected: " + dungeonInfo.getName() + " (" + dungeonInfo.getId() + ")");
                }
        );
    }
}
