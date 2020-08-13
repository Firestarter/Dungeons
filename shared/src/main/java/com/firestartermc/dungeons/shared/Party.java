package com.firestartermc.dungeons.shared;

import java.util.Set;
import java.util.UUID;

public interface Party {
    UUID getLeader();

    Set<UUID> getMembers();

    boolean addMember();

    void sendMessage(String message);
}
