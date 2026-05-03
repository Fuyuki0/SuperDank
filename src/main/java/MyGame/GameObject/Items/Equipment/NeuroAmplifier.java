package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class NeuroAmplifier implements Item {

    @Override
    public String getName() {
        return "Neuro Amplifier";
    }

    @Override
    public String getDescription() {
        return "Increase Exp and Coin by 30%";
    }

    public void applyBuff(World world) {
        world.getPlayer().addExpMultiplier(0.3);
        world.getPlayer().addCoinMultiplier(0.3);
    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}



    private ItemRarity itemRarity = ItemRarity.Modified;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }
}
