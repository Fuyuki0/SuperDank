package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class PlasmaCore extends Weapon {
    public PlasmaCore() {
        super(WeaponType.DAMAGE, AmountType.Multiplier, 0.05);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusDamage(5 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatDamage(5 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusDamage(5 * allBonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "PlasmaCore";
    }
}
