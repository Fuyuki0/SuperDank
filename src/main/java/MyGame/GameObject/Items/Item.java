package MyGame.GameObject.Items;

import MyGame.GameObject.Player;
import MyGame.World;

public interface Item {
    String getName();
    String getDescription();
    void applyBuff(World world);
    public void updateEffect(double deltaTime, World world, Player player);
    ItemRarity getItemRarity();
    void setItemRarity(ItemRarity itemRarity);
}
