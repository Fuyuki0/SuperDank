package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class ServoMotor implements Item {
    @Override
    public String getName() {
        return "Servo Motor";
    }

    @Override
    public String getDescription() {
        return "Skill cost less Stamina!";
    }

    public void applyBuff(World world) {
        world.getPlayer().setSkillStaminaMultiplier(world.getPlayer().getSkillStaminaMultiplier() - 0.4);
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
