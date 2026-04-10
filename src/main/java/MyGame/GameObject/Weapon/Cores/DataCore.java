package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class DataCore extends Weapon {
    public DataCore() {
        super(WeaponType.EXP, AmountType.Multiplier, 0.1);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Experience"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().addExpMultiplier(0.1 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "DataCore";
    }
}
