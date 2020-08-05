package com.firestartermc.dungeons.listeners;

import com.firestartermc.dungeons.DungeonsLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        DungeonsLobby.getNpcManager().sendSpawnPacket(event.getPlayer());
        DungeonsLobby.getDungeonLobby().getPacketReader().inject(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        DungeonsLobby.getDungeonLobby().getPacketReader().unInject(event.getPlayer());
    }

}
