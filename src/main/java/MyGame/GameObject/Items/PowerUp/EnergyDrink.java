package MyGame.GameObject.Items.PowerUp;

import MyGame.GameObject.GameObject;
import MyGame.Interface.Collectable;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;
import MyGame.GameObject.Weapon.Weapon;
import MyGame.Interface.Renderable;
import MyGame.Game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnergyDrink extends GameObject implements Collectable, Renderable {
    private int drinkType;

    public EnergyDrink(double posX, double posY) {
        super(posX, posY, 15);
        this.drinkType = (int) (Math.random() * 3);
    }
    @Override
    public void update(double deltaTime) { }

    @Override
    public void onCollect(Player player, World world) {
        if (drinkType == 0) {
            world.setDrinkShieldTimer(5.0);
            player.setShielded(true);
        } else if (drinkType == 1) {
            world.setDrinkSpeedTimer(5.0);
            if (!world.isDrinkSpeedActive()) {
                world.setDrinkSpeedActive(true);
                player.addSpeedMultiplier(1.0);
                player.setStatAtkSpeed(player.getStatAtkSpeed() + 20);
                for (Weapon weapon : world.getWeaponList()) weapon.setBonusAttackSpeed(weapon.getBonusAttackSpeed() + 20);
            }
        } else if (drinkType == 2) {
            world.setDrinkMagnetTimer(5.0);
        }
    }

    public int getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(int drinkType) {
        this.drinkType = drinkType;
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        if (posX - cameraPosX > -margin && posX - cameraPosX < screenWidth + margin && posY - cameraPosY > -margin && posY - cameraPosY < screenHeight + margin) {
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            double shadowWidth = 30;
            double shadowHeight = 15;
            gc.fillOval(posX - cameraPosX - (shadowWidth / 2.0), posY - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);
            double hoverY = Math.sin(System.currentTimeMillis() / 300.0) * 5;
            double frameWidth = 24;
            double frameHeight = 48;
            double sourceX = 12;
            double sourceY = 252;
            if (drinkType == 0) {
                sourceX = 12 + frameWidth * 2;
                sourceY = 252;
            } else if (drinkType == 1) {
                sourceX = 12 + frameWidth;
                sourceY = 252;
            } else if (drinkType == 2) {
                sourceX = 12;
                sourceY = 252;
            }
            gc.save();
            gc.translate(posX - cameraPosX, posY - cameraPosY - hoverY);
            gc.drawImage(
                    engine.getPropsImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
            );
            gc.restore();
        }
    }
}