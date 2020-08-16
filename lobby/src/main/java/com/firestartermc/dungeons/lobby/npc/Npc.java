package com.firestartermc.dungeons.lobby.npc;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Npc {

    private String id;
    private final String name;
    private Location location;
    private SkinData skinData;
    private boolean hidden;
    private final String dungeonId;

    private EntityPlayer mcEntity;

    public Npc(String id, String name, Location location, SkinData skinData, boolean hidden, String dungeonId) {
        this.id = id.toLowerCase();
        this.name = name;
        this.location = location;
        this.skinData = skinData;
        this.hidden = hidden;
        this.dungeonId = dungeonId;
    }

    public String getId() {
        return id;
    }

    public int getEntityId() {
        return this.mcEntity != null ? this.mcEntity.getId() : -1;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public SkinData getSkinData() {
        return skinData;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getDungeonId() {
        return dungeonId;
    }

    public boolean hasSpawned() {
        return this.mcEntity != null;
    }

    public void spawn() {
        if (this.mcEntity != null) return;
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.name);
        gameProfile.getProperties().put("textures", new Property("textures", this.skinData.getTexture(), this.skinData.getSignature()));

        this.mcEntity = new EntityPlayer(server, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        this.mcEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        if (this.hidden) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnPacket(player);
        }
    }

    public void despawn() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendDespawnPacket(player);
        }
        this.mcEntity = null;
    }

    public void respawn() {
        despawn();
        spawn();
    }

    public void move(Location newLoc) {
        if (this.mcEntity == null) return;

        this.location = newLoc;
        this.mcEntity.setLocation(newLoc.getX(), newLoc.getY(), newLoc.getZ(), newLoc.getYaw(), newLoc.getPitch());

        try (Connection connection = DungeonsLobby.getNpcManager().localStorage.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE npc SET loc_w = ?, loc_x = ?, loc_y = ?, loc_z = ?, loc_yaw = ?, loc_pitch = ? WHERE id = ?;");
            statement.setString(1, newLoc.getWorld().getName());
            statement.setDouble(2, newLoc.getX());
            statement.setDouble(3, newLoc.getY());
            statement.setDouble(4, newLoc.getZ());
            statement.setFloat(5, newLoc.getYaw());
            statement.setFloat(6, newLoc.getPitch());
            statement.setString(7, this.getId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (this.hidden) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.sendMovementPacket(player);
        }
    }

    public boolean show() {
        if (!this.hidden) return false;
        this.hidden = false;
        this.updateHidden();

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnPacket(player);
        }
        return true;
    }

    public boolean hide() {
        if (this.hidden) return false;
        this.hidden = true;
        this.updateHidden();

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendDespawnPacket(player);
        }
        return true;
    }

    public void changeSkin(SkinData skinData) {
        this.skinData = skinData;
        respawn();
    }

    protected void sendSpawnPacket(Player player) {
        if (this.mcEntity == null || this.hidden) return;

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.mcEntity));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.mcEntity));
        connection.sendPacket(new PacketPlayOutEntityTeleport(this.mcEntity));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.mcEntity, (byte) (this.mcEntity.yaw * 256 / 360)));

        Bukkit.getScheduler().runTaskLater(DungeonsLobby.getInstance(), () -> {
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.mcEntity));
        }, 20L);
    }

    protected void sendDespawnPacket(Player player) {
        if (this.mcEntity == null) return;

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(getEntityId()));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.mcEntity));
    }

    private void sendMovementPacket(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityTeleport(this.mcEntity));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.mcEntity, (byte) (this.mcEntity.yaw * 256 / 360)));
    }

    private void updateHidden() {
        try (Connection connection = DungeonsLobby.getNpcManager().localStorage.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE npc SET hidden = ? WHERE id = ?;");
            statement.setBoolean(1, this.hidden);
            statement.setString(2, this.getId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
