package MyGame.GameObject.Items;

import MyGame.GameObject.Items.Equipment.*;
import MyGame.GameObject.Items.PowerUp.Steak;
import MyGame.GameObject.Player.Player;
import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.Game.World;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static Item openingChest(World world) {
        Player player = world.getPlayer();
        world.setGameStop(true);
        List<Item> possibleLoot = new ArrayList<>();
        // stock
        possibleLoot.add(new DataFragment());
        possibleLoot.add(new EngineOverload());
        possibleLoot.add(new MagnetRing());
        possibleLoot.add(new PhantomDrive());
        possibleLoot.add(new ServoMotor());
        possibleLoot.add(new Steak());
        // modified
        possibleLoot.add(new AdrenalSyringe());
        possibleLoot.add(new HexEditor());
        possibleLoot.add(new MagneticRepulsor());
        possibleLoot.add(new NeuroAmplifier());
        possibleLoot.add(new TungstenPlating());
        // prototype
        possibleLoot.add(new NecroticHeart());
        possibleLoot.add(new ParasiticGas());
        possibleLoot.add(new SoulHarvester());
        possibleLoot.add(new TitaniumArmor());
        // artifact
        possibleLoot.add(new DeepestVoid());
        possibleLoot.add(new Doomsday());
        possibleLoot.add(new MutatedBrain());
        // ultimate
        possibleLoot.add(new FinalWeapon());

        ItemRarity rolledRarity = chestGachaRarity(player);
        List<Item> storeLoot = new ArrayList<>();
        Item rolledItem = null;
        for (Item item : possibleLoot) {
            if (item.getItemRarity() == rolledRarity && !world.hasItem((item.getName()))) {
                storeLoot.add(item);
                rolledItem = item;
            }
        }
        if (!storeLoot.isEmpty()) {
            world.setItem(storeLoot.get((int)(Math.random() * storeLoot.size())));
        } else {
            world.setItem(new Item() {
                private final ItemRarity rarity = rolledRarity;

                @Override public String getName() {
                    if (rarity == ItemRarity.Ultimate) return "Da Crown";
                    return "Coins";
                }

                @Override public String getDescription() {
                    if (rarity == ItemRarity.Stock) return "Gained 100 Coins!";
                    if (rarity == ItemRarity.Modified) return "Gained 5,000 Coins!";
                    if (rarity == ItemRarity.Prototype) return "Gained 100,000 Coins!";
                    if (rarity == ItemRarity.Artifact) return "Gained 5,000,000 Coins!";
                    return "You have obtained everything.";
                }

                @Override public void applyBuff(World world) {
                    if (rarity == ItemRarity.Stock) player.setCoin(player.getCoin() + 100);
                    if (rarity == ItemRarity.Modified) player.setCoin(player.getCoin() + 5000);
                    if (rarity == ItemRarity.Prototype) player.setCoin(player.getCoin() + 100000);
                    if (rarity == ItemRarity.Artifact) player.setCoin(player.getCoin() + 5000000);
                    if (rarity == ItemRarity.Ultimate) {}
                }

                @Override
                public void updateEffect(double deltaTime, World world, Player player) {}
                @Override public ItemRarity getItemRarity() {return rarity;}
                @Override public void setItemRarity(ItemRarity itemRarity) {}
            });
        }
        return rolledItem;
    }

    public static ItemRarity chestGachaRarity(Player player) {
        double luckBonus = (player != null) ? player.getLuck() : 0;
        double roll = (Math.random() * 100) + luckBonus;

        if (roll <= 50) return ItemRarity.Stock;
        if (roll <= 80) return ItemRarity.Modified;
        if (roll <= 94) return ItemRarity.Prototype;
        if (roll <= 99) return ItemRarity.Artifact;
        return ItemRarity.Ultimate;

        //if (roll <= 1) return ItemRarity.Stock;
        //if (roll <= 2) return ItemRarity.Modified;
        //if (roll <= 3) return ItemRarity.Prototype;
        //if (roll <= 99) return ItemRarity.Artifact;
        //return ItemRarity.Ultimate;

    }
}
