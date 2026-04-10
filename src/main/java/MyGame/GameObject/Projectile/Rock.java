package MyGame.GameObject.Projectile;

import MyGame.World;

import javax.xml.stream.events.StartDocument;


public class Rock{
    private int pierceCount = 30;
    private boolean isBroken;
    private double spawnTimer;
    private double spawnDuration = 0.5;

    private double currentRelativeAngle;
    private double targetRelativeAngle;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Rock(double angle) {
        this.isBroken = false;
        this.spawnTimer = 0;

        this.currentRelativeAngle = angle;
        this.targetRelativeAngle = angle;
    }

    public void update(double deltaTime, World world) {
        if (spawnTimer < spawnDuration) {
            spawnTimer += deltaTime;
        }
        double diff = targetRelativeAngle - currentRelativeAngle;
        if (diff > 180) diff -= 360;
        if (diff < -180) diff += 360;
        currentRelativeAngle += diff * 10 * deltaTime;
        currentRelativeAngle = (currentRelativeAngle + 360) % 360;

        animationTimer += deltaTime;
        if (animationTimer > 0.02) {
            currentFrame++;
            if (currentFrame >= 10) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }

    }

    public double spawn() {
        if (spawnTimer >= spawnDuration) return 1;
        return spawnTimer / spawnDuration;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void hitEnemy() {
        if (!isBroken) {
            pierceCount--;
            if (pierceCount <= 0) {
                isBroken = true;
            }
        }
    }

    public String type() {
        return "Rock";
    }

    public int getPierceCount() {
        return pierceCount;
    }

    public void setPierceCount(int pierceCount) {
        this.pierceCount = pierceCount;
    }

    public double getSpawnTimer() {
        return spawnTimer;
    }

    public void setSpawnTimer(double spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public double getSpawnDuration() {
        return spawnDuration;
    }

    public void setSpawnDuration(double spawnDuration) {
        this.spawnDuration = spawnDuration;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public double getCurrentRelativeAngle() {
        return currentRelativeAngle;
    }

    public void setCurrentRelativeAngle(double currentRelativeAngle) {
        this.currentRelativeAngle = currentRelativeAngle;
    }

    public double getTargetRelativeAngle() {
        return targetRelativeAngle;
    }

    public void setTargetRelativeAngle(double targetRelativeAngle) {
        this.targetRelativeAngle = targetRelativeAngle;
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
}
