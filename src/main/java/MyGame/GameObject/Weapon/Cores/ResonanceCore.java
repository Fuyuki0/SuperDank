package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class ResonanceCore extends Weapon {
    public ResonanceCore() {
        super(WeaponType.ATKSPEED, AmountType.Multiplier, 0.015);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Attack Speed"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusAttackSpeed(1.5 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatAtkSpeed(1.5 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusAttackSpeed(1.5 * allBonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "ResonanceCore";
    }
}