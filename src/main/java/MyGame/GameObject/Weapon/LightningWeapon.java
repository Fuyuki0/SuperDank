package MyGame.GameObject.Weapon;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player;
import MyGame.GameObject.Projectile.LightningEffect;
import MyGame.GameObject.SoundManager;
import MyGame.World;

import java.util.ArrayList;
import java.util.List;

public class LightningWeapon extends Weapon {
    public LightningWeapon() {
        super(3.0, WeaponType.LIGHTNING);
        this.amount = 8;
        this.bonusCritRate = 2;
        this.bonusCritDmg = 30;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Crit Rate", "Crit Dmg", "Proj Count"};
    }

    @Override
    public void update(double deltaTime, World world, boolean attackWhileJumping) {
        super.update(deltaTime, world, true);
    }

    @Override
    public void playerAttack(World world) {
        Player player = world.getPlayer();
        SoundManager.lightningSound.play();
        List<Enemy> enemies = new ArrayList<>(world.getEnemies());
        if (enemies.isEmpty()) return;

        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy : enemies) {
            double distanceX = enemy.getPosX() - player.getPosX();
            double distanceY = enemy.getPosY() - player.getPosY();
            double distance = distanceX * distanceX + distanceY * distanceY;
            if (distance < 1000 * 1000) enemiesInRange.add(enemy);
        }
        if (enemiesInRange.isEmpty()) return;

        Enemy currentTarget = enemiesInRange.get((int)(Math.random() * enemiesInRange.size()));
        enemies.remove(currentTarget);

        List<double[]> strikePoints = new ArrayList<>();
        strikePoints.add(new double[]{player.getPosX(), player.getPosY()});

        // chain
        int totalJumps = (int) (amount + bonusProjCount);
        for (int i = 0; i <= totalJumps; i++) {
            if (currentTarget == null) break;

            strikePoints.add(new double[]{currentTarget.getPosX(), currentTarget.getPosY()});

            double damage = 80 * (1 + bonusDamage / 100);
            if (Math.random() < bonusCritRate / 100) {
                damage *= (1 + bonusCritDmg / 100);
                currentTarget.takeDamageAndEffectPlayer(player, damage, world.getDamageTexts(), true);
            } else {
                currentTarget.takeDamageAndEffectPlayer(player, damage, world.getDamageTexts(), false);
            }

            currentTarget.smoothHitboxContactPushOut(-currentTarget.getFaceToX() * 50, -currentTarget.getFaceToY() * 50, 0.1);

            if (i == totalJumps) break;

            // next
            Enemy nextTarget = null;
            double closestDistance = 600 * 600;
            for (Enemy e : enemies) {
                double distanceX = e.getPosX() - currentTarget.getPosX();
                double distanceY = e.getPosY() - currentTarget.getPosY();
                double distance = distanceX * distanceX + distanceY * distanceY;
                if (distance < closestDistance) {
                    closestDistance = distance;
                    nextTarget = e;
                }
            }
            currentTarget = nextTarget;
            if (currentTarget != null) enemies.remove(currentTarget);
        }
        world.getLightningEffects().add(new LightningEffect(strikePoints));
    }

    @Override
    public String getName() {
        return "Shock Collar";
    }
}