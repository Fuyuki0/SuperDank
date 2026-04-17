package MyGame.GameObject.Projectile;

import MyGame.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.SoundManager;
import MyGame.GameObject.Weapon.OrbitRock;
import MyGame.World;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;


public class Rock extends Projectile {
    private int pierceCount = 30;
    private boolean isBroken;
    private double spawnTimer;
    private double spawnDuration = 0.5;

    private double currentRelativeAngle;
    private double targetRelativeAngle;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Rock(double angle) {
        super(angle);
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

    @Override
    protected void update(double deltaTime) {
    }

    @Override
    public void updateProj(double deltaTime, World world) {
        double currentAngle = world.getOrbitRock().getAngle() + this.getCurrentRelativeAngle();
        double currentRadius = world.getOrbitRock().getRADIUS() * this.spawn();

        double rockPosX = world.getPlayer().getPosX() + Math.cos(Math.toRadians(currentAngle)) * currentRadius;
        double rockPosY = world.getPlayer().getPosY() + Math.sin(Math.toRadians(currentAngle)) * currentRadius;

        for (int j = 0; j < world.getEnemies().size(); j++) {
            Enemy enemy = world.getEnemies().get(j);
            double distanceX = enemy.getPosX() - rockPosX;
            double distanceY = enemy.getPosY() - rockPosY;
            double distance = (distanceX * distanceX + distanceY * distanceY);
            double rockRadius = 40 * (1 + world.getOrbitRock().getBonusSize() / 100);
            if ((distance < rockRadius * rockRadius) && !world.getPlayer().isJumping()) {
                double damage = 20 * (1 + world.getOrbitRock().getBonusDamage() / 100);
                enemy.takeDamageAndEffectPlayer(world.getPlayer(), damage, world.getDamageTexts(), false);
                enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 200, -enemy.getFaceToY() * 200, 0.1);
                SoundManager.fireballSound.play();
                this.hitEnemy();
                if (this.isBroken()) break;
            }
        }
    }

    @Override
    public boolean isDone() {
        return isBroken;
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

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, GameEngine engine) {
        OrbitRock orbitRock = engine.getWorld().getOrbitRock();
        double frameWidth = 64;
        double frameHeight = 64;
        if (orbitRock != null) {
            List<Rock> rocks = orbitRock.getRocks();
            if (!rocks.isEmpty()) {
                for (int i = rocks.size() - 1; i >= 0; i--) {
                    Rock rock = rocks.get(i);
                    double sourceX = 0 + rock.getCurrentFrame() * frameWidth;
                    double sourceY = 0;
                    gc.save();
                    gc.setGlobalAlpha(Math.max(rock.spawn(), 90));
                    double currentAngle = orbitRock.getAngle() + rock.getCurrentRelativeAngle();
                    double currentRadius = orbitRock.getRADIUS() * rock.spawn();
                    double rockPosX = engine.getWorld().getPlayer().getPosX() - cameraPosX + Math.cos(Math.toRadians(currentAngle)) * currentRadius;
                    double rockPosY = engine.getWorld().getPlayer().getPosY() - cameraPosY - engine.getWorld().getPlayer().getPosZ() + Math.sin(Math.toRadians(currentAngle)) * currentRadius;
                    double scaleRock = 1.2 + (1 + orbitRock.getBonusSize() / 100) * 1.2;
                    gc.translate(rockPosX, rockPosY);
                    gc.rotate(currentAngle + 90);
                    gc.drawImage(
                            engine.getRockImage(),
                            sourceX, sourceY, frameWidth, frameHeight,
                            -(frameWidth * scaleRock / 2), -(frameHeight * scaleRock / 2.0), frameWidth * scaleRock, frameHeight * scaleRock
                    );
                    gc.restore();
                }
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
