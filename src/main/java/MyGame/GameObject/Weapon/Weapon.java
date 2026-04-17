package MyGame.GameObject.Weapon;

import MyGame.Rarity;
import MyGame.World;

public abstract class Weapon {
    protected double cooldown;
    protected double timer;

    protected double activeDuration;
    protected double activeTimer;
    protected boolean hasActiveTimer;

    protected boolean attackWhileJumping = false;

    protected WeaponType type;
    protected int level;
    protected Rarity rarity;
    protected int maxLevel = 20;

    protected double amount;
    public enum AmountType {Multiplier, Amount}
    protected AmountType amountType;
    protected double allBonus = 0;
    protected boolean isCore = false;

    protected double bonusDamage = 0;
    protected double bonusAttackSpeed = 0;
    protected double bonusCritRate = 0;
    protected double bonusCritDmg = 0;
    protected double bonusSize = 0;
    protected double bonusProjCount = 0;

    public Weapon() {
        this.level = 1;
    }

    public Weapon(WeaponType type) {
        this.level = 1;
        this.type = type;
    }

    public Weapon(WeaponType type, AmountType amountType, double amount) {
        this.level = 1;
        this.type = type;
        this.amountType = amountType;
        this.amount = amount;
    }

    public Weapon(double cooldown, WeaponType type) {
        this.level = 1;
        this.type = type;
        this.cooldown = cooldown;
        this.timer = 0;
        this.hasActiveTimer = false;
        this.attackWhileJumping = false;
    }

    public Weapon(double cooldown, double activeDuration, WeaponType type) {
        this.level = 1;
        this.type = type;
        this.cooldown = cooldown;
        this.timer = 0;
        this.hasActiveTimer = true;
        this.activeDuration = activeDuration;
        this.activeTimer = 0;
    }

    public void update(double deltaTime, World world) {
        timer -= deltaTime;

        if (hasActiveTimer) {
            if (activeTimer > 0) {
                if (!this.attackWhileJumping) {
                    if (!world.getPlayer().isJumping())
                        activeTimer -= deltaTime;
                } else
                    activeTimer -= deltaTime;
            }
        }

        if (timer <= 0) {
            this.playerAttack(world);
            double totalAtkSpeed = this.bonusAttackSpeed + world.getPlayer().getStatAtkSpeed();
            timer = cooldown * Math.max(0.1, 1.0 - (totalAtkSpeed / 100));

            if (hasActiveTimer) {
                activeTimer = activeDuration;
            }
        }

    }

    public void coreUpgrade(World world, double bonus) {}
    public void applyCoreEffect(Weapon weapon, double allBonus) {}

    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Crit Rate", "Crit Dmg", "Size", "Proj Count"};
    }
    public void addBonusDamage(double amount) {this.bonusDamage += amount;}
    public void addBonusAttackSpeed(double amount) {
        this.bonusAttackSpeed += amount;
    }
    public void addBonusCritRate(double amount) {this.bonusCritRate += amount;}
    public void addBonusCritDmg(double amount) {this.bonusCritDmg += amount;}
    public void addBonusSize(double amount) {this.bonusSize += amount;}
    public void addBonusProjCount(double amount) {
        this.bonusProjCount += amount;
        }

    protected abstract void playerAttack(World world);
    public abstract String getName();


    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getActiveDuration() {
        return activeDuration;
    }

    public void setActiveDuration(double activeDuration) {
        this.activeDuration = activeDuration;
    }

    public double getActiveTimer() {
        return activeTimer;
    }

    public void setActiveTimer(double activeTimer) {
        this.activeTimer = activeTimer;
    }

    public boolean isHasActiveTimer() {
        return hasActiveTimer;
    }

    public void setHasActiveTimer(boolean hasActiveTimer) {
        this.hasActiveTimer = hasActiveTimer;
    }

    public boolean isAttackWhileJumping() {
        return attackWhileJumping;
    }

    public void setAttackWhileJumping(boolean attackWhileJumping) {
        this.attackWhileJumping = attackWhileJumping;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public WeaponType getType() {
        return type;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public double getBonusDamage() {
        return bonusDamage;
    }

    public void setBonusDamage(double bonusDamage) {
        this.bonusDamage = bonusDamage;
    }

    public double getBonusAttackSpeed() {
        return bonusAttackSpeed;
    }

    public void setBonusAttackSpeed(double bonusAttackSpeed) {
        this.bonusAttackSpeed = bonusAttackSpeed;
    }

    public double getBonusCritRate() {
        return bonusCritRate;
    }

    public void setBonusCritRate(double bonusCritRate) {
        this.bonusCritRate = bonusCritRate;
    }

    public double getBonusCritDmg() {
        return bonusCritDmg;
    }

    public void setBonusCritDmg(double bonusCritDmg) {
        this.bonusCritDmg = bonusCritDmg;
    }

    public double getBonusSize() {
        return bonusSize;
    }

    public void setBonusSize(double bonusSize) {
        this.bonusSize = bonusSize;
    }

    public double getBonusProjCount() {
        return bonusProjCount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBonusProjCount(double bonusProjCount) {
        this.bonusProjCount = bonusProjCount;
    }

    public AmountType getAmountType() {
        return amountType;
    }

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
    }

    public boolean isCore() {
        return isCore;
    }


    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setCore(boolean core) {
        isCore = core;
    }

    public double getAllBonus() {
        return allBonus;
    }

    public void setAllBonus(double allBonus) {
        this.allBonus = allBonus;
    }
}
