package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class ChaosCore extends Weapon {
    public ChaosCore() {
        super(WeaponType.CHOAS, AmountType.Multiplier, 0.15);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Difficulty"};
    }



    @Override
    public void coreUpgrade(World world, double bonus) {
        world.setDifficultyMultiplier(world.getDifficultyMultiplier() + 0.15 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "ChaosCore";
    }
}
