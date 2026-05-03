package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class VectorCore extends Weapon {
    public VectorCore() {
        super(WeaponType.CRITRATE, AmountType.Multiplier, 0.05);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Crit Rate"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusCritRate(5 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatCritRate(5 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusCritRate(5 * allBonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "VectorCore";
    }
}
