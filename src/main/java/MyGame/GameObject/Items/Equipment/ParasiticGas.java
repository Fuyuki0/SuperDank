package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

import static MyGame.Game.Main.SCREEN_HEIGHT;
import static MyGame.Game.Main.SCREEN_WIDTH;

public class ParasiticGas implements Item {
    private double parasiticGasTimer = 5.0;

    @Override
    public String getName() {
        return "Parasitic Gas";
    }

    @Override
    public String getDescription() {
        return "Drains 5% amount of HP every 5 second from enemies and heal yourself!";
    }

    public void applyBuff(World world) {

    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        double cameraPosX = player.getPosX() - (SCREEN_WIDTH / 2.0);
        double cameraPosY = player.getPosY() - (SCREEN_HEIGHT / 2.0);
        parasiticGasTimer -= deltaTime;
        if (parasiticGasTimer <= 0) {
            parasiticGasTimer = 5.0;
            world.setGasVisualTimer(0.8);
            double healthDrain = 0;
            for (Enemy enemy : world.getEnemies()) {
                double distanceX = enemy.getPosX() - cameraPosX;
                double distanceY = enemy.getPosY() - cameraPosY;
                if (distanceX > 0 && distanceX < SCREEN_WIDTH && distanceY > 0 && distanceY < SCREEN_HEIGHT) {
                    double damage;
                    if (enemy.isBoss()) {
                        damage = enemy.getMaxHealth() * 0.01;
                    } else
                        damage = enemy.getMaxHealth() * 0.05;
                    enemy.takeDamageAndEffectPlayer(player, damage, world.getDamageTexts(), false);
                    healthDrain += damage;
                }
            }
            player.heal((int) (healthDrain));
        }
    }

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
