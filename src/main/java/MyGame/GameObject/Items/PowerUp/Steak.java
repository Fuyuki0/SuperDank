package MyGame.GameObject.Items.PowerUp;

import MyGame.GameObject.GameObject;
import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class Steak extends GameObject implements Item {

    private int healAmount;
    public Steak(double PosX, double PosY) {
        super(PosX, PosY, 20);
        this.healAmount = 600;
    }

    public Steak() {
        this.healAmount = 600;
    }

    @Override
    public String getName() {
        return "Steak";
    }

    @Override
    public String getDescription() {
        return "Steak get store in your inventory turn into 300 Overheal!";
    }

    @Override
    public void applyBuff(World world) {
        world.getPlayer().addOverHealing(3000);
    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}

    public void update(double deltaTime) {}

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    private ItemRarity itemRarity = ItemRarity.Stock;

    public ItemRarity getItemRarity() {
        return itemRarity;
    }
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }
}
