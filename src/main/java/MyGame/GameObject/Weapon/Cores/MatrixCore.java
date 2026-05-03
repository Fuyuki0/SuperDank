package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class MatrixCore extends Weapon {
    public MatrixCore() {
        super(WeaponType.SIZE, AmountType.Multiplier, 0.1);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Size"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusSize(10 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatSize(10 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusSize(10 * allBonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "MatrixCore";
    }
}
