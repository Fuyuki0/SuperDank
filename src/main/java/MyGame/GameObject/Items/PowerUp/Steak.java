package MyGame.GameObject.Items.PowerUp;

import MyGame.GameObject.GameObject;
import MyGame.Interface.Item;
import MyGame.Rarity.ItemRarity;
import MyGame.GameObject.Player.Player;
import MyGame.Interface.Collectable;
import MyGame.Interface.Renderable;
import MyGame.Game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import MyGame.Game.World;

public class Steak extends GameObject implements Item, Collectable, Renderable {

    private int healAmount;
    public Steak(double PosX, double PosY) {
        super(PosX, PosY, 20);
        this.healAmount = 600;
    }

    public Steak() {
        this.healAmount = 600;
    }

    @Override
    public String getName() {
        return "Steak";
    }

    @Override
    public String getDescription() {
        return "Steak get store in your inventory turn into 300 Overheal!";
    }

    @Override
    public void applyBuff(World world) {
        world.getPlayer().addOverHealing(3000);
    }

    @Override
    public void updateEffect(double deltaTime, World world, Player player) {}

    public void update(double deltaTime) {}

    @Override
    public void onCollect(Player player, World world) {
        if (player.getMaxHealth() > player.getCurrentHealth()) {
            player.setCurrentHealth(player.getCurrentHealth() + this.getHealAmount());
        }
    }


    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    private ItemRarity itemRarity = ItemRarity.Stock;

    public ItemRarity getItemRarity() {
        return itemRarity;
    }
    public void setItemRarity(ItemRarity itemRarity) {
        this.itemRarity = itemRarity;
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        if (posX - cameraPosX > -margin && posX - cameraPosX < screenWidth + margin && posY - cameraPosY > -margin && posY - cameraPosY < screenHeight + margin) {
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            double shadowWidth = 45;
            double shadowHeight = 15;
            gc.fillOval(posX - cameraPosX - (shadowWidth / 2.0), posY - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);

            double hoverY = Math.sin(System.currentTimeMillis() / 400.0) * 5;
            gc.save();
            gc.translate(posX - cameraPosX, posY - cameraPosY - hoverY);
            double frameWidth = 44;
            double frameHeight = 40;
            double sourceX = 324;
            double sourceY = 204;
            gc.drawImage(
                    engine.getPropsImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
            );
            gc.restore();
        }
    }
}
