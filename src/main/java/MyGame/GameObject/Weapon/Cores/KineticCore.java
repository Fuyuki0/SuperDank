package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class KineticCore extends Weapon {
    public KineticCore() {
        super(WeaponType.MOVEMENTSPD, AmountType.Multiplier, 0.2);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Movement Speed"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().addSpeedMultiplier(0.2 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "KineticCore";
    }
}
