package com.firestartermc.dungeons.common;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.Set;

public class Party {

    private final Player leader;
    private Set<Player> members = Sets.newHashSet();

    public Party(Player leader) {
        this.leader = leader;
    }

    public Player getLeader() {
        return leader;
    }

    public void addMember(Player player) {
        this.members.add(player);
    }

    public Set<Player> getMembers() {
        return members;
    }
}
