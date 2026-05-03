package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class FractalCore extends Weapon {
    public FractalCore() {
        super(WeaponType.PROJCOUNT, AmountType.Amount, 1);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Proj Count"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        for (Weapon weapon : world.getWeaponList()) {
            weapon.addBonusProjCount(1 * bonus);
        }
        allBonus += bonus;
        world.getPlayer().setStatProjCount(1 * allBonus);
    }

    @Override
    public void applyCoreEffect(Weapon weapon, double allBonus) {
        weapon.addBonusProjCount(1 * allBonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "FractalCore";
    }
}
