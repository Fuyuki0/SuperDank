package MyGame.GameObject.Items.Equipment;

import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.GameObject.Weapon.OrbitRock;
import MyGame.GameObject.Weapon.Weapon;
import MyGame.Game.World;

import java.util.List;

public class Doomsday implements Item {
    private double doomsdayTimer = 2.0;
    private int doomsdayLastStat = -1;
    @Override
    public String getName() {
        return "Doomsday";
    }

    @Override
    public String getDescription() {
        return "Every 2 second you will obtain 200% random stat";
    }

    public  void applyBuff(World world) {
    }
    @Override
    public void updateEffect(double deltaTime, World world, Player player)  {
        doomsdayTimer -= deltaTime;
        List<Weapon> weaponList = world.getWeaponList();
        OrbitRock orbitRock = world.getOrbitRock();
        if (doomsdayTimer <= 0) {
            doomsdayTimer = 2.0;
            if (doomsdayLastStat != -1) {
                switch (doomsdayLastStat) {
                    case 0 -> {
                        for (Weapon weapon : weaponList)
                            weapon.setBonusDamage(weapon.getBonusDamage() - 200);
                        player.setStatDamage(player.getStatDamage() - 200);
                    }
                    case 1 -> {
                        for (Weapon weapon : weaponList)
                            weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() - 200);
                        player.setStatAtkSpeed(player.getStatAtkSpeed() - 200);
                    }
                    case 2 -> {
                        for (Weapon weapon : weaponList)
                            weapon.setBonusSize(weapon.getBonusSize() - 200);
                        player.setStatSize(player.getStatSize() - 200);
                    }
                    case 3 -> {
                        for (Weapon weapon : weaponList)
                            weapon.setBonusCritDmg(weapon.getBonusCritDmg() - 200);
                        player.setStatCritDamage(player.getStatCritDamage() - 200);
                    }
                    case 4 -> {
                        for (Weapon weapon : weaponList)
                            weapon.setBonusCritRate(weapon.getBonusCritRate() - 200);
                        player.setStatCritRate(player.getStatCritRate() - 200);
                    }
                    case 5 -> {
                        for (Weapon weapon : weaponList)
                            if (weapon == orbitRock) {
                                orbitRock.setAmount(orbitRock.getAmount() - 200);
                                orbitRock.setRespawnTime(1.5);
                            } else
                                weapon.setBonusProjCount(weapon.getBonusProjCount() - 200);
                        player.setStatProjCount(player.getStatProjCount() - 200);
                    }
                    case 6 -> {
                        player.addSpeedMultiplier(-2);
                    }
                    case 7 -> {
                        player.addLifeSteal(-2);
                    }
                    case 8 -> {
                        player.addExpMultiplier(-2);
                    }
                    case 9 -> {
                        player.addCoinMultiplier(-2);
                    }
                    case 10 -> {
                        player.addRegeneration(-200);
                    }
                }
            }
            doomsdayLastStat = (int) (Math.random() * 11);
            switch (doomsdayLastStat) {
                case 0 -> {
                    for (Weapon weapon : weaponList)
                        weapon.setBonusDamage(weapon.getBonusDamage() + 200);
                    player.setStatDamage(player.getStatDamage() + 200);
                }
                case 1 -> {
                    for (Weapon weapon : weaponList)
                        weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() + 200);
                    player.setStatAtkSpeed(player.getStatAtkSpeed() + 200);
                }
                case 2 -> {
                    for (Weapon weapon : weaponList)
                        weapon.setBonusSize(weapon.getBonusSize() + 200);
                    player.setStatSize(player.getStatSize() + 200);
                }
                case 3 -> {
                    for (Weapon weapon : weaponList)
                        weapon.setBonusCritDmg(weapon.getBonusCritDmg() + 200);
                    player.setStatCritDamage(player.getStatCritDamage() + 200);
                }
                case 4 -> {
                    for (Weapon weapon : weaponList)
                        weapon.setBonusCritRate(weapon.getBonusCritRate() + 200);
                    player.setStatCritRate(player.getStatCritRate() + 200);
                }
                case 5 -> {
                    for (Weapon weapon : weaponList)
                        if (weapon == orbitRock) {
                            orbitRock.setAmount(orbitRock.getAmount() + 200);
                            orbitRock.setRespawnTime(0.01);
                        }else
                            weapon.setBonusProjCount(weapon.getBonusProjCount() + 200);
                    player.setStatProjCount(player.getStatProjCount() + 200);
                }
                case 6 -> {
                    player.addSpeedMultiplier(2);
                }
                case 7 -> {
                    player.addLifeSteal(2);
                }
                case 8 -> {
                    player.addExpMultiplier(2);
                }
                case 9 -> {
                    player.addCoinMultiplier(2);
                }
                case 10 -> {
                    player.addRegeneration(200);
                }
            }
        }
    }

    private ItemRarity itemRarity = ItemRarity.Artifact;

    @Override
    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    @Override
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }

}
