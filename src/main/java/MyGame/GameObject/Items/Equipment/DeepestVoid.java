package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class DeepestVoid implements Item {
    @Override
    public String getName() {
        return "Deepest Void";
    }

    @Override
    public String getDescription() {
        return "Have 20% chance to spawn a BlackHole on enemy if the attack is critical attack!";
    }

    public void applyBuff(World world) {
        world.getPlayer().setHasDeepestVoid(true);
    }


    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}

    private ItemRarity itemRarity = ItemRarity.Artifact;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }

}
