package MyGame.GameObject.Skill;

import MyGame.GameObject.Enemies.Enemy;
import java.util.ArrayList;
import java.util.List;

public class CrossSlash {
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

    public boolean isFade() {
        return timer <= 0;
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