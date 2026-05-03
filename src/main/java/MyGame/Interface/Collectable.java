package MyGame.Interface;

import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public interface Collectable {
    void onCollect(Player player, World world);

    default void updateCollectible(Player player, World world, double deltaTime) {}
}

    
    
