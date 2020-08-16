package com.firestartermc.dungeons.lobby.npc;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.firestartermc.kerosene.data.db.LocalStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NpcManager {

    protected final LocalStorage localStorage;
    private boolean failedToInstantiate;

    private final Map<String, Npc> NPCs;

    public NpcManager() {
        this.failedToInstantiate = false;
        this.localStorage = new LocalStorage("npc");
        this.NPCs = Maps.newHashMap();

        try (Connection connection = this.localStorage.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE_TABLE);
            ps.execute();
        } catch (SQLException e) {
            this.failedToInstantiate = true;
            e.printStackTrace();
        }

        if (!failedToInstantiate) {
            loadNPCs();
        }
    }

    private void loadNPCs() {
        try (Connection connection = this.localStorage.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Npc npc = new Npc(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        new Location(
                                Bukkit.getWorld(resultSet.getString(3)),
                                resultSet.getDouble(4),
                                resultSet.getDouble(5),
                                resultSet.getDouble(6),
                                resultSet.getFloat(7),
                                resultSet.getFloat(8)
                        ),
                        new SkinData(
                                resultSet.getString(9),
                                resultSet.getString(10)
                        ),
                        resultSet.getBoolean(11),
                        resultSet.getString(12)
                );
                this.NPCs.put(npc.getId(), npc);
            }
        } catch (SQLException ex) {
            this.failedToInstantiate = true;
            ex.printStackTrace();
        }
    }

    public boolean createNpc(String id, String name, String dungeonId, Location location) {
        id = id.toLowerCase();

        if (this.NPCs.containsKey(id)) {
            return false;
        }

        Npc newNpc = new Npc(id, name, location, SkinData.DEFAULT, false, dungeonId);
        try (Connection connection = this.localStorage.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, newNpc.getId());
            statement.setString(2, newNpc.getName());
            statement.setString(3, newNpc.getLocation().getWorld().getName());
            statement.setDouble(4, newNpc.getLocation().getX());
            statement.setDouble(5, newNpc.getLocation().getY());
            statement.setDouble(6, newNpc.getLocation().getZ());
            statement.setFloat(7, newNpc.getLocation().getYaw());
            statement.setFloat(8, newNpc.getLocation().getPitch());
            statement.setString(9, newNpc.getSkinData().getTexture());
            statement.setString(10, newNpc.getSkinData().getSignature());
            statement.setBoolean(11, newNpc.isHidden());
            statement.setString(12, newNpc.getDungeonId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        newNpc.spawn();
        this.NPCs.put(id, newNpc);
        return true;
    }

    public Npc getNpc(String id) {
        return this.NPCs.get(id.toLowerCase());
    }

    public Set<String> getNpcIds() {
        return this.NPCs.keySet();
    }

    public void spawnAll() {
        this.NPCs.values().forEach(Npc::spawn);
    }

    public void despawnAll() {
        this.NPCs.values().forEach(Npc::despawn);
    }

    public void respawnAll() {
        this.NPCs.values().forEach(Npc::respawn);
    }

    public void sendSpawnPackets(Player player) {
        this.NPCs.values().forEach(npc -> npc.sendSpawnPacket(player));
    }

    public boolean remove(Npc npc) {
        npc.hide();
        npc.despawn();

        try (Connection connection = this.localStorage.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setString(1, npc.getId());
            statement.execute();
            this.NPCs.remove(npc.getId());
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS npc (" +
            "id TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "loc_w TEXT NOT NULL, " +
            "loc_x REAL NOT NULL, " +
            "loc_y REAL NOT NULL, " +
            "loc_z REAL NOT NULL, " +
            "loc_yaw REAL NOT NULL, " +
            "loc_pitch REAL NOT NULL, " +
            "skin_texture TEXT, " +
            "skin_signature INTEGER, " +
            "hidden INTEGER NOT NULL, " +
            "dungeon_id TEXT" +
            ");";
    private final static String SQL_SELECT_ALL = "SELECT * FROM npc;";
    private final static String SQL_INSERT = "INSERT INTO npc " +
            "(id, name, loc_w, loc_x, loc_y, loc_z, loc_yaw, loc_pitch, skin_texture, skin_signature, hidden, dungeon_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final static String SQL_DELETE = "DELETE FROM npc WHERE id = ?;";
}
