package MyGame.GameObject.Items.Collectable;

import java.util.ArrayList;
import java.util.List;

import MyGame.GameObject.GameObject;
import MyGame.GameObject.Obstacle;
import MyGame.GameObject.Player.Player;
import MyGame.Interface.Collectable;
import MyGame.Interface.Renderable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import MyGame.Game.GameEngine;
import MyGame.Game.World;

public class Experience extends GameObject implements Collectable, Renderable {
    private int expValue;
    private int expType;

    private static final List<Experience> pool = new ArrayList<>();

    public static Experience create(double posX, double posY) {
        if (pool.isEmpty()) return new Experience(posX, posY);
        Experience exp = pool.remove(pool.size() - 1);
        exp.init(posX, posY);
        return exp;
    }

    public static void release(Experience experience) {
        pool.add(experience);
    }

    public Experience(double posX, double posY) {
        super(posX, posY, 40);
        init(posX, posY);
    }

    public void init(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        double check = Math.random() * 100;
        double expGain = 5;
        if (check <= 60) {
            this.expType = 0;
            this.expValue = (int)expGain;
        } else if (check <= 88) {
            this.expType = 1;
            this.expValue = (int)expGain * 3;
        } else if (check <= 98) {
            this.expType = 2;
            this.expValue = (int)expGain * 6;
        } else if (check <= 100) {
            this.expType = 3;
            this.expValue = (int)expGain * 20;
        }
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public boolean checkCollision(GameObject object) {
        return super.checkCollision(object);
    }

    @Override
    public void updateCollectible(Player player, World world, double deltaTime) {
        for (Obstacle obstacle : world.getObstacles()) {
            double left = obstacle.getPosX();
            double right = obstacle.getPosX() + obstacle.getWidth();
            double top = obstacle.getPosY();
            double bottom = obstacle.getPosY() + obstacle.getHeight();

            if (posX > left && posX < right && posY > top && posY < bottom) {
                double distanceLeft = posX - left;
                double distanceRight = right - posX;
                double distanceTop = posY - top;
                double distanceBottom = bottom - posY;
                double min = Math.min(Math.min(distanceLeft, distanceRight), Math.min(distanceTop, distanceBottom));
                if (min == distanceLeft) posX = left - hitbox;
                else if (min == distanceRight) posX = right + hitbox;
                else if (min == distanceTop) posY = top - hitbox;
                else if (min == distanceBottom) posY = bottom + hitbox;
            }
        }

        double distancePosX = player.getPosX() - posX;
        double distancePosY = player.getPosY() - posY;
        double distance = Math.sqrt(distancePosX * distancePosX + distancePosY * distancePosY);
        if ((distance < player.getMagnetRadius() || world.getDrinkMagnetTimer() > 0) && !player.isJumping()) {
            double pullingSpeed = 600 * deltaTime;
            posX += (distancePosX / distance) * pullingSpeed;
            posY += (distancePosY / distance) * pullingSpeed;
        }
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        if (posX - cameraPosX > -margin && posX - cameraPosX < screenWidth + margin && posY - cameraPosY > -margin && posY - cameraPosY < screenHeight + margin) {
            double frameWidth = 32;
            double frameHeight = 32;
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            double shadowWidth = 25;
            double shadowHeight = 10;
            gc.fillOval(posX - cameraPosX - (shadowWidth / 2.0), posY - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);

            gc.save();
            ColorAdjust maxColor = new ColorAdjust();
            maxColor.setSaturation(1.5);
            gc.setEffect(maxColor);
            double sourceX = frameWidth * expType;
            double sourceY = 0;
            double hoverY = Math.sin(System.currentTimeMillis() / 250.0) * 5;
            gc.translate(posX - cameraPosX, posY - cameraPosY - hoverY);
            double hue = (System.currentTimeMillis() % 2000) / 1000.0 - 1;
            if (expType == 4) {
                ColorAdjust rainbow = new ColorAdjust();
                rainbow.setHue(hue);
                rainbow.setSaturation(1.2);
                gc.setEffect(rainbow);
                gc.scale(1.5, 1.5);
            }
            gc.drawImage(
                    engine.getGemImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2), -(frameHeight / 2), frameWidth, frameHeight
            );
            gc.restore();
        }
    }

    @Override
    public void onCollect(Player player, World world) {
        player.addExperience(expValue);
        Experience.release(this);
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public int getExpType() {
        return expType;
    }

    public void setExpType(int expType) {
        this.expType = expType;
    }

}
