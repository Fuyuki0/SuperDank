package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Items.Item;
import MyGame.GameObject.Items.ItemRarity;
import MyGame.GameObject.Player;
import MyGame.World;

import java.util.List;

import static MyGame.Main.SCREEN_HEIGHT;
import static MyGame.Main.SCREEN_WIDTH;

public class FinalWeapon implements Item {
    private double finalWeaponTimer = 0;

    @Override
    public String getName() {
        return "Final Weapon";
    }

    @Override
    public String getDescription() {
        return "*** WARNING *** this item will give you schizophrenia";
    }

    public void applyBuff(World world) {
        world.getPlayer().setHasFinalWeapon(true);
    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        double cameraPosX = player.getPosX() - (SCREEN_WIDTH / 2.0);
        double cameraPosY = player.getPosY() - (SCREEN_HEIGHT / 2.0);
        List<double[]> glitchStrikes = world.getGlitchStrikes();
        finalWeaponTimer -= deltaTime;
        if (finalWeaponTimer <= 0) {
            finalWeaponTimer = 0.15;
            int spawnCount = 2 + (int) (Math.random() * 3);
            for (int s = 0; s < spawnCount; s++) {
                double glitchX = cameraPosX + (Math.random() * SCREEN_WIDTH);
                double glitchY = cameraPosY + (Math.random() * SCREEN_HEIGHT);
                double glitchRadius = 150 + (Math.random() * 100);
                double hue = Math.random() * 360;

                glitchStrikes.add(new double[]{glitchX, glitchY, glitchRadius, 0.3, hue});

                for (Enemy enemy : world.getEnemies()) {
                    double distanceX = enemy.getPosX() - glitchX;
                    double distanceY = enemy.getPosY() - glitchY;
                    if (distanceX * distanceX + distanceY * distanceY < glitchRadius * glitchRadius) {
                        double damage;
                        if (enemy.getHealth() > 1) {
                            damage = enemy.isBoss() ? enemy.getHealth() * 0.005 : enemy.getHealth() * 0.5;
                        } else {
                            damage = 1.0;
                        }
                        enemy.takeDamageAndEffectPlayer(player, damage, world.getDamageTexts(), true);
                        enemy.smoothHitboxContactPushOut(player.getFaceToX() * 50, player.getFaceToY() * 50, 0.1);
                    }
                }
            }
        }

        for (int i = glitchStrikes.size() - 1; i >= 0; i--) {
            double[] g = glitchStrikes.get(i);
            g[3] -= deltaTime;
            if (g[3] <= 0) glitchStrikes.remove(i);
        }
    }

    private ItemRarity itemRarity = ItemRarity.Ultimate;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }

}
