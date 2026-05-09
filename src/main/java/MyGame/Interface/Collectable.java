package MyGame.Interface;

import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

/**
 * An interface for items that the player can pick up in the game world.
 */
public interface Collectable {
    void onCollect(Player player, World world);

    default void updateCollectible(Player player, World world, double deltaTime) {}
}

    
    
