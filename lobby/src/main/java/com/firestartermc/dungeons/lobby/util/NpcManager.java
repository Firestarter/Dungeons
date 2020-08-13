package com.firestartermc.dungeons.lobby.util;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.firestartermc.dungeons.lobby.gui.DungeonSelectorGui;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NpcManager {

    private UUID uuid;
    private EntityPlayer mcPlayer;
    private SkinData skinData;
    private boolean isHidden;

    public NpcManager() {
        if (DungeonsLobby.getInstance().getConfig().contains("npc")) {
            spawn();
        }
    }

    public void spawn() {
        this.uuid = UUID.randomUUID();

        ConfigurationSection npc = DungeonsLobby.getInstance().getConfig().getConfigurationSection("npc");
        this.isHidden = npc.getBoolean("hidden", false);
        String name = ChatColor.translateAlternateColorCodes('&', npc.getString("name", "&bDungeoneer"));

        ConfigurationSection skinConfig = npc.getConfigurationSection("skin");
        this.skinData = new SkinData(skinConfig.getString("texture"), skinConfig.getString("signature"));

        ConfigurationSection locationConfig = npc.getConfigurationSection("location");
        Location location = new Location(
                Bukkit.getWorld(locationConfig.getString("world")),
                locationConfig.getDouble("x"),
                locationConfig.getDouble("y"),
                locationConfig.getDouble("z"),
                (float) locationConfig.getDouble("yaw"),
                (float) locationConfig.getDouble("pitch")
        );

        if (location == null) {
            System.out.println("Location was null, failed to spawn npc.");
        }

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(this.uuid, name);
        gameProfile.getProperties().put("textures", new Property("textures", this.skinData.texture, this.skinData.signature));

        this.mcPlayer = new EntityPlayer(server, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        this.mcPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnPacket(player);
        }
    }

    public void despawn() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendDespawnPacket(player);
        }

        this.mcPlayer = null;
        this.uuid = null;
    }

    public void reload() {
        despawn();
        spawn();
    }

    public void sendSpawnPacket(Player player) {
        if (this.mcPlayer == null || isHidden) return;
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.mcPlayer));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.mcPlayer));
        connection.sendPacket(new PacketPlayOutEntityTeleport(this.mcPlayer));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.mcPlayer, (byte) (this.mcPlayer.yaw * 256 / 360)));

        Bukkit.getScheduler().runTaskLater(DungeonsLobby.getInstance(), () -> {
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.mcPlayer));
        }, 20L);
    }

    public void sendDespawnPacket(Player player) {
        if (this.mcPlayer == null) return;
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(getEntityId()));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.mcPlayer));
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getEntityId() {
        return this.mcPlayer != null ? this.mcPlayer.getId() : -200;
    }

    public boolean hasSpawned() {
        return this.mcPlayer != null;
    }

    public void move(Location loc) {
        ConfigurationSection section = DungeonsLobby.getInstance().getConfig().getConfigurationSection("npc.location");
        section.set("world", loc.getWorld().getName());
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
        DungeonsLobby.getInstance().saveConfig();

        if (this.mcPlayer != null) {
            this.mcPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityTeleport(this.mcPlayer));
                connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.mcPlayer, (byte) (this.mcPlayer.yaw * 256 / 360)));
            }
        }
    }

    public void handleNpcClick(Player player) {
        DungeonSelectorGui.open(player);
    }

    public boolean hide() {
        if (this.isHidden) {
            return false;
        }
        this.isHidden = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendDespawnPacket(player);
        }
        DungeonsLobby.getInstance().getConfig().set("npc.hidden", true);
        DungeonsLobby.getInstance().saveConfig();
        return true;
    }

    public boolean show() {
        if (!this.isHidden) {
            return false;
        }
        this.isHidden = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnPacket(player);
        }
        DungeonsLobby.getInstance().getConfig().set("npc.hidden", false);
        DungeonsLobby.getInstance().saveConfig();
        return true;
    }

    // change skin

    public static class SkinData {
        private final String texture;
        private final String signature;

        public SkinData(String texture, String signature) {
            this.texture = texture;
            this.signature = signature;
        }

        public String getTexture() {
            return texture;
        }

        public String getSignature() {
            return signature;
        }
    }
}
