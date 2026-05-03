package MyGame.GameObject.Weapon;

import MyGame.GameObject.Enemies.CompareEnemies;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;
import MyGame.GameObject.Projectile.Boomerang;
import MyGame.Game.SoundManager;
import MyGame.Game.World;

import java.util.ArrayList;
import java.util.List;

public class BoomerangWeapon extends Weapon {
    public BoomerangWeapon() {
        super(3, WeaponType.BOOMERANG);
        this.bonusCritDmg = 50;
        this.bonusCritRate = 5;
        this.amount = 3;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Crit Rate", "Crit Dmg", "Size", "Proj Count"};
    }

    @Override
    public void playerAttack(World world) {
        Player player = world.getPlayer();
        SoundManager.shurikenSound.play();
        List<Enemy> enemies = new ArrayList<>(world.getEnemies());

        if (enemies.isEmpty()) return;
        CompareEnemies compareObject = new CompareEnemies(player, enemies);
        compareObject.useComparator(enemies);

        int shootAmount = Math.min((int)amount + (int)bonusProjCount, enemies.size());
        for (int i = 0; i < shootAmount; i++) {
            Enemy target = enemies.get(i);
            world.getProjectiles().add(new Boomerang(player.getPosX(), player.getPosY(), target.getPosX(), target.getPosY(), 1 + bonusSize / 100));
        }
    }

    public String getName() {
        return "Spinner";
    }

}
