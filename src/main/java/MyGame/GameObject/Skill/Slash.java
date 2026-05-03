package MyGame.GameObject.Skill;

import MyGame.Game.GameEngine;
import MyGame.Interface.ActiveSkill;
import MyGame.Game.World;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;
import javafx.scene.canvas.GraphicsContext;

public class Slash implements ActiveSkill {
    private double posX;
    private double posY;
    private double posZ;
    private double angle;
    private double timer;
    private final double MAX_TIMER = 0.2;
    private boolean slashHit;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Slash(double posX, double posY, double posZ, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.angle = angle;
        this.timer = MAX_TIMER;
        this.slashHit = false;
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        this.animationTimer += deltaTime;
        if (animationTimer > 0.02) {
            currentFrame++;
            if (currentFrame >= 7) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    @Override
    public void updateSkill(double deltaTime, World world) {
        update(deltaTime);
        Player player = world.getPlayer();
        if (!isSlashHit()) {
            for (Enemy enemy : world.getEnemies()) {
                double distancePosX = enemy.getPosX() - getPosX();
                double distancePosY = enemy.getPosY() - getPosY();
                double distance = Math.sqrt(distancePosX * distancePosX + distancePosY * distancePosY);
                if (distance < 700) {
                    double slashCheckFront = (distancePosX / distance) * Math.cos(Math.toRadians(getAngle())) + (distancePosY / distance) * Math.sin(Math.toRadians(getAngle()));
                    if (slashCheckFront > 0.90) {
                        double slashDamage = 60 * (1 + player.getStatDamage() / 100);
                        enemy.takeDamageAndEffectPlayer(player, slashDamage, world.getDamageTexts(), false);
                        enemy.smoothHitboxContactPushOut(Math.cos(Math.toRadians(getAngle())) * 200,
                                Math.sin(Math.toRadians(getAngle())) * 200,
                                0.08
                        );
                        setSlashHit(true);
                    }
                }
            }
            if (isSlashHit())
                player.setCurrentStamina(player.getCurrentStamina() - 5);
        }
    }


    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 128 * 6;
        double frameHeight = 128 * 6;
        double scaleSlash = 1.0;
        double sourceX = 0, sourceY = 0;

        if (currentFrame == 1) {
            sourceX = frameWidth * 1;
            sourceY = frameHeight * 0;
        } else if (currentFrame == 2) {
            sourceX = frameWidth * 0;
            sourceY = frameHeight * 1;
        } else if (currentFrame == 3) {
            sourceX = frameWidth * 0;
            sourceY = frameHeight * 3;
        } else if (currentFrame == 4) {
            sourceX = frameWidth * 1;
            sourceY = frameHeight * 3;
        } else if (currentFrame == 5) {
            sourceX = frameWidth * 0;
            sourceY = frameHeight * 4;
        } else if (currentFrame == 6) {
            sourceX = frameWidth * 1;
            sourceY = frameHeight * 4;
            return;
        }

        gc.save();
        gc.setGlobalAlpha(opacity());
        gc.translate(posX - cameraPosX, posY - cameraPosY - posZ);
        gc.save();
        gc.rotate(angle);
        gc.translate(300, 0);
        gc.rotate(40 + 180);
        gc.drawImage(
                engine.getSlashImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth * scaleSlash / 2), -(frameHeight * scaleSlash / 2.0), frameWidth * scaleSlash, frameHeight * scaleSlash
        );
        gc.restore();
        gc.restore();
    }

    @Override
    public boolean isFinished() {
        return timer <= 0;
    }

    public boolean fade() {
        return isFinished();
    }

    public double opacity() {
        return Math.max(0, timer / MAX_TIMER);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getMAX_TIMER() {
        return MAX_TIMER;
    }

    public boolean isSlashHit() {
        return slashHit;
    }

    public void setSlashHit(boolean slashHit) {
        this.slashHit = slashHit;
    }

    public double getPosZ() {
        return posZ;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public double getAnimationTimer() {
        return animationTimer;
    }

    public void setAnimationTimer(double animationTimer) {
        this.animationTimer = animationTimer;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

}
