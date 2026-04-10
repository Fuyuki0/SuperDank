package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

public class MagneticRepulsor implements Item {
    private double magneticRepulsorTimer = 3.0;

    @Override
    public String getName() {
        return "Magnetic Repulsor";
    }

    @Override
    public String getDescription() {
        return "Push nearby enemies away from you for every 3 second!";
    }

    public void applyBuff(World world) {

    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        magneticRepulsorTimer -= deltaTime;
        if (magneticRepulsorTimer <= 0) {
            magneticRepulsorTimer = 3.0;
            world.setRepulsorVisualTimer(0.4);
            for (Enemy enemy : world.getEnemies()) {
                double distanceX = enemy.getPosX() - player.getPosX();
                double distanceY = enemy.getPosY() - player.getPosY();
                if (distanceX * distanceX + distanceY * distanceY < 400 * 400)
                    enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 6000, -enemy.getFaceToY() * 6000, 2);
            }
        }
    }

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
