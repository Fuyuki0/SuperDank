package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class BiocatalystCore extends Weapon {
    public BiocatalystCore() {
        super(WeaponType.REGEN, AmountType.Amount, 2);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Regen"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().addRegeneration((int) (amount * bonus));
        allBonus += bonus;
    }


    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "BiocatalystCore";
    }
}
