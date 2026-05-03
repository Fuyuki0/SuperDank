package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.GameObject.Weapon.Weapon;
import MyGame.Game.World;

import java.util.List;

public class AdrenalSyringe implements Item {
    private double adrenalTimer = 0;
    private boolean adrenalActive = false;

    @Override
    public String getName() {
        return "Adrenal Syringe";
    }

    @Override
    public String getDescription() {
        return "If your health lower than 20% give you 50% Lifesteal, Attack speed and Damage for 3 second!";
    }

    public void applyBuff(World world) {

    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        List<Weapon> weaponList = world.getWeaponList();
        double adrenalCooldown = world.getAdrenalCooldown();
        if (player.healthRatio() < 0.2 && !adrenalActive && adrenalCooldown <= 0) {
            adrenalActive = true;
            adrenalTimer = 3.0;
            player.addLifeSteal(0.5);
            for (Weapon weapon : weaponList)
                weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() + 50);
            for (Weapon weapon : weaponList)
                weapon.setBonusDamage(weapon.getBonusDamage() + 50);
            player.setStatAtkSpeed(player.getStatAtkSpeed() + 50);
            player.setStatDamage(player.getStatDamage() + 50);
        }
        if (adrenalActive) {
            adrenalTimer -= deltaTime;
            if (adrenalTimer <= 0) {
                adrenalActive = false;
                adrenalCooldown = 60;
                player.addLifeSteal(-0.5);
                for (Weapon weapon : weaponList)
                    weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() - 50);
                for (Weapon weapon : weaponList)
                    weapon.setBonusDamage(weapon.getBonusDamage() - 50);
                player.setStatAtkSpeed(player.getStatAtkSpeed() - 50);
                player.setStatDamage(player.getStatDamage() - 50);
            }
        }
    }

    private ItemRarity itemRarity = ItemRarity.Modified;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }
}
