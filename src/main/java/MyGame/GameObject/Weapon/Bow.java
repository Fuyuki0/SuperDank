package MyGame.GameObject.Weapon;

import MyGame.GameObject.CompareEnemies;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player;
import MyGame.GameObject.Projectile.Arrow;
import MyGame.GameObject.SoundManager;
import MyGame.World;

import java.util.ArrayList;
import java.util.List;

public class Bow extends Weapon {
    private double amount;
    public Bow() {
        super(2, WeaponType.BOW);
        this.amount = 2;
        this.bonusCritDmg = 50;
        this.bonusCritRate = 5;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Crit Rate", "Crit Dmg", "Proj Count"};
    }

    @Override
    public void playerAttack(World world) {
        Player player = world.getPlayer();
        SoundManager.arrowSound.play();
        List<Enemy> enemies = new ArrayList<>(world.getEnemies());

        if (enemies.isEmpty()) return;
        CompareEnemies compareObject = new CompareEnemies(player, enemies);
        compareObject.useComparator(enemies);

        int shootAmount = Math.min((int) amount + (int)bonusProjCount, enemies.size());
        for (int i = 0; i < shootAmount; i++) {
            Enemy target = enemies.get(i);
            world.getProjectiles().add(new Arrow(player.getPosX(), player.getPosY(), target.getPosX(), target.getPosY()));
        }
    }

    public String getName() {
        return "Bau";
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
