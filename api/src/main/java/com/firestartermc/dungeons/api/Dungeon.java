package com.firestartermc.dungeons.api;

public interface Dungeon {

    /**
     * Setup the dungeon.
     * <p>Everything that should happen before players get teleported.</p>
     */
    void setup();

    /**
     * Start the dungeon
     * <p>Runs when the players have been teleported to the dungeon.</p>
     */
    void start();

    /**
     * End the dungeon
     * <p>Runs when the players are still in the dungeon, but it should end.</p>
     */
    void end();

    /**
     * Cleanup
     * <p>Runs the dungeon has fully ended and the players have been teleported out.</p>
     */
    void cleanup();

}
