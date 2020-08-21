package com.firestartermc.dungeons.server.loader.injector;

import com.firestartermc.dungeons.shared.Party;
import com.google.inject.Binder;
import com.google.inject.Module;

public class DungeonInjectorModule implements Module {

    private final Party party;

    public DungeonInjectorModule(Party party) {
        this.party = party;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Party.class).toInstance(party);
        // TODO map manager
        // TODO Dungeon Manager
    }

}
