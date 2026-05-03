package MyGame.GameObject.Items.Equipment;

import MyGame.GameObject.Items.Collectable.Experience;
import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

import static MyGame.Game.Main.SCREEN_HEIGHT;
import static MyGame.Game.Main.SCREEN_WIDTH;

public class HexEditor implements Item {
    private double hexEditorTimer = 10.0;

    @Override
    public String getName() {
        return "Hex Editor";
    }

    @Override
    public String getDescription() {
        return "Every 30 second turn all Exp on the vision to higher rarity";
    }

    public void applyBuff(World world) {

    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {
        double cameraPosX = player.getPosX() - (SCREEN_WIDTH / 2.0);
        double cameraPosY = player.getPosY() - (SCREEN_HEIGHT / 2.0);
        this.hexEditorTimer -= deltaTime;
        if (hexEditorTimer <= 0) {
            hexEditorTimer = 30.0;
            for (Experience exp : world.getExperience()) {
                double distanceX = exp.getPosX() - cameraPosX;
                double distanceY = exp.getPosY() - cameraPosY;
                if (distanceX > 0 && distanceX < SCREEN_WIDTH && distanceY > 0 && distanceY < SCREEN_HEIGHT && exp.getExpType() < 3) {
                    exp.setExpType(exp.getExpType() + 1);
                    exp.setExpValue(exp.getExpValue() * 3);
                }
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
