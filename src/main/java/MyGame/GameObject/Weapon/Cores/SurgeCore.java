package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class SurgeCore extends Weapon {
    public SurgeCore() {
        super(WeaponType.OVERHEAL, AmountType.Amount, 2000);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Overheal"};
    }

     @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().setMaxOverHeal(world.getPlayer().getMaxOverHeal() + 200 * bonus);
        if (world.getPlayer().getCurrentOverHeal() <= 0) {
            world.getPlayer().setCurrentOverHeal(0);
        }
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "SurgeCore";
    }
}
