package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class TitaniumArmor implements Item {
    @Override
    public String getName() {
        return "Titanium Armor";
    }

    @Override
    public String getDescription() {
        return "Increase Health by 4000 and immune to being slow down by enemies but decrease speed and stamina by 50%";
    }

    public void applyBuff(World world) {
        world.getPlayer().setMaxHealth(world.getPlayer().getMaxHealth() + 4000);
        world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth() + 4000);
        world.getPlayer().addSpeedMultiplier(-0.5);
        world.getPlayer().setMaxStamina(world.getPlayer().getMaxStamina() * 0.5);
        world.getPlayer().setImmuneToSlow(true);
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
