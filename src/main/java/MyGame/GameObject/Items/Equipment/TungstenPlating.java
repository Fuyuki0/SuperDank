package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class TungstenPlating implements Item {
    @Override
    public String getName() {
        return "Tungsten Plating";
    }

    @Override
    public String getDescription() {
        return "Reduce incoming damage when dash by 40%";
    }

    public void applyBuff(World world) {
        world.getPlayer().setDashDamageReduction(world.getPlayer().getDashDamageReduction() + 0.4);
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
