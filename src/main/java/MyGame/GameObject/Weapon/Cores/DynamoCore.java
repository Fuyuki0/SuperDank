package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.World;

public class DynamoCore extends Weapon {
    public DynamoCore() {
        super(WeaponType.STAMINA, AmountType.Amount, 20);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Stamina"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().setMaxStamina(world.getPlayer().getMaxStamina() + 20 * bonus);
        world.getPlayer().setCurrentStamina(world.getPlayer().getCurrentStamina() + 20 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "DynamoCore";
    }
}
