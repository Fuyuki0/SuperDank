package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class MutatedBrain implements Item {
    private double mutatedBrainTimer = 3.0;



    @Override
    public String getName() {
        return "Mutated Brain";
    }

    @Override
    public String getDescription() {
        return "Got random movement every 3 second but obtain 3 more slots for items";
    }

    public void applyBuff(World world) {
        world.getPlayer().setMaxItemSlot(world.getPlayer().getMaxItemSlot() + 3);
    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        mutatedBrainTimer -= deltaTime;
        if (mutatedBrainTimer <= 0) {
            mutatedBrainTimer = 3.0;
            player.setPosX(player.getPosX() + (Math.random() - 0.5) * 800);
            player.setPosY(player.getPosY() + (Math.random() - 0.5) * 800);
            world.setRainbowTimer(0.5);
        }
    }

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
