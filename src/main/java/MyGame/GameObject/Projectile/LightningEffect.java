package MyGame.GameObject.Projectile;

import MyGame.World;

import java.util.List;

public class LightningEffect extends Projectile {
    private List<double[]> strikePoints;
    private double timer;
    private final double MAX_TIMER = 0.3; // Fades out in 0.3 seconds

    private int currentFrame = 0;
    private int currentFrame_2 = 0;
    private double animationTimer = 0.0;
    private double animationTimer2 = 0.0;

    public LightningEffect(List<double[]> strikePoints) {
        super(strikePoints);
        this.strikePoints = strikePoints;
        this.timer = MAX_TIMER;
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        animationTimer += deltaTime;
        if (animationTimer > 0.07) {
            currentFrame++;
            if (currentFrame >= 4) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
        animationTimer2 += deltaTime;
        if (animationTimer2 > MAX_TIMER / 30){
            currentFrame_2++;
            if (currentFrame_2 >= 30) {
                currentFrame_2 = 0;
            }
            animationTimer2 = 0;
        }
    }

    @Override
    public void updateProj(double deltaTime, World world) {
        this.update(deltaTime);
    }

    @Override
    public boolean isDone() {
        return this.isFade();
    }

    public boolean isFade() {
        return timer <= 0;
    }

    public double opacity() {
        return Math.max(0, timer / MAX_TIMER);
    }

    public void setStrikePoints(List<double[]> strikePoints) {
        this.strikePoints = strikePoints;
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

    public List<double[]> getStrikePoints() {
        return strikePoints;
    }

    public int getCurrentFrame_2() {
        return currentFrame_2;
    }

    public void setCurrentFrame_2(int currentFrame_2) {
        this.currentFrame_2 = currentFrame_2;
    }
}