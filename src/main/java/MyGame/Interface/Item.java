package MyGame.Interface;

import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public interface Item {
    String getName();
    String getDescription();
    void applyBuff(World world);
    void updateEffect(double deltaTime, World world, Player player);
    ItemRarity getItemRarity();
    void setItemRarity(ItemRarity itemRarity);
}
