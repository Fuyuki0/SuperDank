package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class PhantomDrive implements Item {
    @Override
    public String getName() {
        return "Phantom Drive";
    }

    @Override
    public String getDescription() {
        return "Increase Dodge Chance by 15%";
    }

    public void applyBuff(World world) {
        world.getPlayer().setDodgeChance(world.getPlayer().getDodgeChance() + 0.15);
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
