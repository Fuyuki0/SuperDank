package MyGame.GameObject.Weapon.Cores;

import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;
import MyGame.Game.World;

public class OverClockCore extends Weapon {
    public OverClockCore() {
        super(WeaponType.COOLDOWN, AmountType.Multiplier, 0.05);
        this.isCore = true;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Cooldown"};
    }

    @Override
    public void coreUpgrade(World world, double bonus) {
        world.getPlayer().setSlashCooldownMultiplier(world.getPlayer().getSlashCooldownMultiplier() + 5 * bonus);
    }

    @Override
    public void playerAttack(World world) {}

    @Override
    public String getName() {
        return "OverClockCore";
    }
}
