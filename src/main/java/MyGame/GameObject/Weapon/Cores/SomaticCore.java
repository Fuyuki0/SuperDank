package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class SomaticCore extends Weapon {
    public SomaticCore() {
        super(WeaponType.HEALTH, AmountType.Amount, 1000);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Health"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().setMaxHealth(world.getPlayer().getMaxHealth() + amount * bonus);
        if (world.getPlayer().getMaxHealth() > world.getPlayer().getCurrentHealth())
            world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth() + amount * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "SomaticCore";
    }
}
