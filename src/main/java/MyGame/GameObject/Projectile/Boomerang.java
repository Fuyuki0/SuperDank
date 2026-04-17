package MyGame.GameObject.Projectile;

import MyGame.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.World;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;


public class Boomerang extends Projectile {
    private double speed = 1000;
    private double velocityX;
    private double velocityY;
    private double faceToX;
    private double faceToY;

    private double timer;
    private final double durationTimer = 4.0;
    private List<Enemy> hitEnemies;
    private double spinningAngle = 0;
    private double smoothTurning = -0.1;

    public Boomerang(double posX, double posY, double enemiesPosX, double enemiesPosY, double multiSize) {
        super(posX, posY, 40.0 * multiSize);
        this.hitEnemies = new ArrayList<>();
        double directionX = enemiesPosX - posX;
        double directionY = enemiesPosY - posY;
        double direction = Math.sqrt(directionX * directionX + directionY * directionY);
        if (direction > 0) {
            this.velocityX = directionX / direction;
            this.velocityY = directionY / direction;
            setFaceToForBoomerang(velocityX, velocityY);
        }
        this.timer = durationTimer;
    }

    public void setFaceToForBoomerang(double velocityX, double velocityY) {
        if (this.velocityX != 0 || this.velocityY != 0) {
            if (timer >= durationTimer * 2.2 / 6) {
                this.faceToX = velocityX;
                this.faceToY = velocityY;
            } else if (timer < durationTimer * 2.2 / 6 && timer > durationTimer * 1 / 6) {
                this.hitEnemies.clear();
                this.faceToX += velocityX * Math.cos(Math.PI / 6) * smoothTurning / 3;
                this.faceToY += velocityY * Math.sin(Math.PI / 6) * smoothTurning / 3;
            }
            double vector = Math.sqrt(faceToX * faceToX + faceToY * faceToY);
            if (vector > 0) {
                faceToX /= vector;
                faceToY /= vector;
            }
        }
    }

    public double opacity() {
        if (durationTimer * 2 / 6.0 > timer) {
            return timer / (durationTimer * 2 / 6.0);
        } else return 1;
    }

    public boolean isBroken() {
        return timer < 0;
    }

    public boolean hitEnemy(Enemy enemy) {
        if (!hitEnemies.contains(enemy)) {
            hitEnemies.add(enemy);
            return true;
        } else return false;
    }

    @Override
    public void update(double deltaTime) {
        timer -= deltaTime;
        speed -= 1000 * deltaTime;
        setFaceToForBoomerang(velocityX, velocityY);
        this.posX += faceToX * speed * deltaTime;
        this.posY += faceToY * speed * deltaTime;
        this.spinningAngle = (this.spinningAngle + (100 * deltaTime) / 2) % 360;
        this.smoothTurning *= 1.005;
    }

    @Override
    public void updateProj(double deltaTime, World world) {
            this.update(deltaTime);

            for (int j = 0; j < world.getEnemies().size(); j++) {
                Enemy enemy = world.getEnemies().get(j);
                if (this.checkCollision(enemy)) {
                    if (this.hitEnemy(enemy)) {
                        double boomerangDamage = 20 * (1 + world.getBoomerangWeapon().getBonusDamage() / 100);
                        if (Math.random() < world.getBoomerangWeapon().getBonusCritRate() / 100) {
                            boomerangDamage *= (1 + world.getBoomerangWeapon().getBonusCritDmg() / 100);
                            enemy.takeDamageAndEffectPlayer(world.getPlayer(), boomerangDamage, world.getDamageTexts(), true);
                        } else {
                            enemy.takeDamageAndEffectPlayer(world.getPlayer(), boomerangDamage, world.getDamageTexts(), false);
                        }
                        enemy.smoothHitboxContactPushOut(this.getFaceToX() * 50, this.getFaceToY() * 50, 0.08);
                    }
                }
            }

    }

    @Override
    public boolean isDone() {
        return this.isBroken();
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, GameEngine engine) {
        double frameWidth = 349;
        double frameHeight = 349;
        double sourceX = 0;
        double sourceY = 0;
        double scaleBoomerang = 0.2 + (engine.getWorld().getBoomerangWeapon().getBonusSize() / 100) / 5;
        gc.save();
        gc.setGlobalAlpha(this.opacity());
        gc.translate(this.getPosX() - cameraPosX, this.getPosY() - cameraPosY);
        gc.rotate(this.getSpinningAngle());
        gc.drawImage(
                engine.getBoomerangImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth * scaleBoomerang / 2), -(frameHeight * scaleBoomerang / 2), frameWidth * scaleBoomerang, frameHeight * scaleBoomerang
        );
        gc.restore();
        gc.setGlobalAlpha(1);
    }


    public double getFaceToY() {
        return faceToY;
    }

    public void setFaceToY(double faceToY) {
        this.faceToY = faceToY;
    }

    public double getFaceToX() {
        return faceToX;
    }

    public void setFaceToX(double faceToX) {
        this.faceToX = faceToX;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getDurationTimer() {
        return durationTimer;
    }

    public List<Enemy> getHitEnemies() {
        return hitEnemies;
    }

    public void setHitEnemies(List<Enemy> hitEnemies) {
        this.hitEnemies = hitEnemies;
    }

    public double getSpinningAngle() {
        return spinningAngle;
    }

    public void setSpinningAngle(double spinningAngle) {
        this.spinningAngle = spinningAngle;
    }
}

