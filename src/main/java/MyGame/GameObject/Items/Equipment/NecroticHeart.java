package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class NecroticHeart implements Item {
    @Override
    public String getName() {
        return "Necrotic Heart";
    }

    @Override
    public String getDescription() {
        return "Enemy have 5% to explode when death!";
    }

    public void applyBuff(World world) {

    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}


    private ItemRarity itemRarity = ItemRarity.Prototype;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }

}
