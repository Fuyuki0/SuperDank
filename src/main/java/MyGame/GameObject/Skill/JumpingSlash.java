package MyGame.GameObject.Skill;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;

import java.util.ArrayList;
import java.util.List;

import MyGame.Interface.ActiveSkill;
import MyGame.Game.World;

public class JumpingSlash implements ActiveSkill {
    private double PosX;
    private double PosY;
    private double PosZ;
    private Player player;
    private List<Enemy> enemyList;
    private double timer;
    private final double MAX_TIMER = 0.2;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public JumpingSlash(double PosX, double PosY, double PosZ, Player player) {
        this.PosX = PosX;
        this.PosY = PosY;
        this.PosZ = PosZ;
        this.player = player;
        this.timer = MAX_TIMER;
        this.enemyList = new ArrayList<>();
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        this.animationTimer += deltaTime;
        if (animationTimer > 0.02) {
            currentFrame++;
            if (currentFrame >= 10) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    @Override
    public void updateSkill(double deltaTime, World world) {
        update(deltaTime);
        for (Enemy enemy : world.getEnemies()) {
            if (!getEnemyList().contains(enemy)) {
                double distanceX = enemy.getPosX() - player.getPosX();
                double distanceY = enemy.getPosY() - player.getPosY();
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if (distance < 300) {
                    enemy.takeDamageAndEffectPlayer(player, 80 * (1 + player.getStatDamage() / 100), world.getDamageTexts(), false);
                    enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 300, -enemy.getFaceToY() * 300, 0.2);
                    getEnemyList().add(enemy);
                    player.setSlashCooldown(player.getSlashCooldown() - 0.01);
                }
            }
        }
        player.setCanJumpingSlashOnce(false);
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 288;
        double frameHeight = 288;
        double scaleJumpingSlash = 1.3;

        double sourceX = frameWidth * getCurrentFrame();
        double sourceY = frameHeight;

        gc.save();
        gc.setGlobalAlpha(opacity());
        gc.translate(getPosX() - cameraPosX, getPosY() - cameraPosY - getPosZ() + 50);
        gc.drawImage(
                engine.getJumpingSlashImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth * scaleJumpingSlash / 2.0), -(frameHeight * scaleJumpingSlash / 2.0), frameWidth * scaleJumpingSlash, frameHeight * scaleJumpingSlash
        );
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public void setEnemyList(List<Enemy> enemyList) {
        this.enemyList = enemyList;
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

    public double getPosX() {
        return PosX;
    }

    public void setPosX(double posX) {
        PosX = posX;
    }

    public double getPosY() {
        return PosY;
    }

    public void setPosY(double posY) {
        PosY = posY;
    }

    public double getPosZ() {
        return PosZ;
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
        PosZ = posZ;
    }

}
