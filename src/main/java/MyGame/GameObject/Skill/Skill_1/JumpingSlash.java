package MyGame.GameObject.Skill.Skill_1;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player;

import java.util.ArrayList;
import java.util.List;

public class JumpingSlash {
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

    public boolean fade() {
        return timer <= 0;
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
