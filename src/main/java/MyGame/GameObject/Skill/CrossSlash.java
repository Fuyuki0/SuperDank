package MyGame.GameObject.Skill;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;
import java.util.ArrayList;
import java.util.List;

import MyGame.Interface.ActiveSkill;
import MyGame.Game.World;

public class CrossSlash implements ActiveSkill {
    private double posX, posY;
    private double angle;
    private double timer;
    private final double MAX_TIMER = 0.4;
    private List<Enemy> hitEnemies;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public CrossSlash(double posX, double posY, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.timer = MAX_TIMER;
        this.hitEnemies = new ArrayList<>();
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        this.animationTimer += deltaTime;
        if (animationTimer > 0.04) {
            currentFrame++;
            if (currentFrame >= 8) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    @Override
    public void updateSkill(double deltaTime, World world) {
        update(deltaTime);
        Player player = world.getPlayer();
        if (!isFinished()) {
            for (Enemy enemy : world.getEnemies()) {
                if (!getHitEnemies().contains(enemy)) {
                    double distanceX = enemy.getPosX() - getPosX();
                    double distanceY = enemy.getPosY() - getPosY();
                    if (distanceX * distanceX + distanceY * distanceY < 300 * 300) {
                        getHitEnemies().add(enemy);
                        double damage = 150 * (1 + player.getStatDamage() / 100);
                        enemy.takeDamageAndEffectPlayer(player, damage, world.getDamageTexts(), false);
                        enemy.smoothHitboxContactPushOut(-player.getFaceToX() * 100, -player.getFaceToY() * 100, 0.2);
                    }
                }
            }
        }
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 128 * 6;
        double frameHeight = 128 * 6;
        double sourceX = 0 + frameWidth * (getCurrentFrame() % 2);
        double sourceY = 0 + frameHeight * (getCurrentFrame() / 2);

        gc.save();
        gc.setGlobalAlpha(opacity());
        gc.translate(getPosX() - cameraPosX, getPosY() - cameraPosY);
        gc.rotate(Math.toDegrees(getAngle()));

        gc.save();
        gc.rotate(-50);
        gc.drawImage(
                engine.getCrossSlashImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth / 2.0), -(frameHeight / 2.0), frameWidth, frameHeight
        );
        gc.restore();

        gc.save();
        gc.scale(1, -1);
        gc.rotate(-50);
        gc.drawImage(
                engine.getCrossSlashImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth / 2.0), -(frameHeight / 2.0), frameWidth, frameHeight
        );
        gc.restore();

        gc.restore();
    }

    @Override
    public boolean isFinished() {
        return timer <= 0;
    }

    public boolean isFade() {
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

    public List<Enemy> getHitEnemies() {
        return hitEnemies;
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

    public void setHitEnemies(List<Enemy> hitEnemies) {
        this.hitEnemies = hitEnemies;
    }
}