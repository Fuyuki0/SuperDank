package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class MagnetRing implements Item {

    @Override
    public String getName() {
        return "Magnet Ring";
    }

    @Override
    public String getDescription() {
        return "increase exp pickup range";
    }

    public void applyBuff(World world) {
        world.getPlayer().setMagnetRadius(world.getPlayer().getMagnetRadius() + 200);
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
