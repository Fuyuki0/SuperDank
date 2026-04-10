package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class EngineOverload implements Item {
    @Override
    public String getName() {
        return "Engine Overload";
    }

    @Override
    public String getDescription() {
        return "Increase movement speed by 30%";
    }

    public void applyBuff(World world) {
        world.getPlayer().addSpeedMultiplier(0.3);
    }


    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}

    private ItemRarity itemRarity = ItemRarity.Stock;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }
}
