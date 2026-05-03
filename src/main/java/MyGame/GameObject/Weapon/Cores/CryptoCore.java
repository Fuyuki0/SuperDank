package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class CryptoCore extends Weapon {
    public CryptoCore() {
        super(WeaponType.COIN, AmountType.Multiplier, 1);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Coin"};
    }


    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().addCoinMultiplier(amount * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "CryptoCore";
    }
}
