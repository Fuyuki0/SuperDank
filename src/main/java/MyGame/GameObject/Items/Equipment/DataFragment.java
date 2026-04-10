package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class DataFragment implements Item {
    @Override
    public String getName() {
        return "Data Fragment";
    }

    @Override
    public String getDescription() {
        return "Increase Exp by 30% !";
    }

    @Override
    public void applyBuff(World world) {
        world.getPlayer().addExpMultiplier(0.3);
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
