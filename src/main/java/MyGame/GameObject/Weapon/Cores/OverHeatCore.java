package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class OverHeatCore extends Weapon {
    public OverHeatCore() {
        super(WeaponType.CRITDMG, AmountType.Multiplier, 0.20);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Crit Damage"};
    }



    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusCritDmg(20 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatCritDamage(20 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusCritDmg(20 * allBonus);
    }


    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "OverHeatCore";
    }
}
