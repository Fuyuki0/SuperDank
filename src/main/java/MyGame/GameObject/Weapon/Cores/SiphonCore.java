package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class SiphonCore extends Weapon {
    public SiphonCore() {
        super(WeaponType.LIFESTEAL, AmountType.Multiplier, 0.01);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"LifeSteal"};
    }

      @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().addLifeSteal(amount * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "SiphonCore";
    }
}
