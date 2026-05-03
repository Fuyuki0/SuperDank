package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class EntropyCore extends Weapon {
    public EntropyCore() {
        super(WeaponType.LUCK, AmountType.Multiplier, 0.10);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Luck"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().setLuck(world.getPlayer().getLuck() + 0.1 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "EntropyCore";
    }
}
