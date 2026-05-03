package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class SoulHarvester implements Item {
    @Override
    public String getName() {
        return "Soul Harvester";
    }

    @Override
    public String getDescription() {
        return "Obtain 0.5% chance to instant kill enemies";
    }

    public void applyBuff(World world) {
        world.getPlayer().setInstantKillChance(world.getPlayer().getInstantKillChance() + 0.005);
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
