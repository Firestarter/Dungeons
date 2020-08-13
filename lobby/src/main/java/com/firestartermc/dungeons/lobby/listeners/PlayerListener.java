package com.firestartermc.dungeons.lobby.listeners;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        DungeonsLobby.getNpcManager().sendSpawnPacket(event.getPlayer());
        DungeonsLobby.getInstance().getPacketReader().inject(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        DungeonsLobby.getInstance().getPacketReader().unInject(event.getPlayer());
    }

}
