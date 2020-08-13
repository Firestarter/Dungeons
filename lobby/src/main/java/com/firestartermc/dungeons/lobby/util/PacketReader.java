package com.firestartermc.dungeons.lobby.util;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.google.common.collect.ImmutableList;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_16_R1.Vec3D;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.nkomarn.kerosene.util.internal.Debug;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader {

    private static final Map<UUID, Channel> channels = new HashMap<>();

    public void inject(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        channels.put(player.getUniqueId(), channel);

        if (channel.pipeline().get("PacketInjector") != null)
            return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, PacketPlayInUseEntity packet, List<Object> list) throws Exception {
                list.add(packet);
                readPacket(player, packet);
            }
        });
    }

    public void unInject(Player player) {
        if (!channels.containsKey(player.getUniqueId())) {
            return;
        }

        Channel channel = channels.remove(player.getUniqueId());
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(Player player, PacketPlayInUseEntity packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase(PacketPlayInUseEntity.class.getSimpleName())) {
            int id = (int) getValue(packet, "a");

            if (DungeonsLobby.getNpcManager().getEntityId() != id) {
                return; // not dungeon entity
            }

            String action = getValue(packet, "action").toString();
            boolean attack = action.equalsIgnoreCase("ATTACK");
            String hand = attack ? null : getValue(packet, "d").toString();

            try {
                Vec3D pos = (Vec3D) getValue(packet, "c");
                String hitPosition = pos == null ? null : pos.toString();
                String sneaking = getValue(packet, "e").toString();

                Debug.sendLines(DungeonsLobby.DEBUG_CATEGORY_NPC_INTERACT, player, () -> ImmutableList.of(
                        "&7Id: &e" + id,
                        "&7Action: &e" + action,
                        "&7Hit Position: &e" + hitPosition,
                        "&7Hand: &e" + hand,
                        "&7Sneaking: &e" + sneaking
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (attack)
                return;
            if (hand != null && hand.equalsIgnoreCase("OFF_HAND"))
                return;

            if (action.equalsIgnoreCase("INTERACT")) {
                DungeonsLobby.getNpcManager().handleNpcClick(player);
            }
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
