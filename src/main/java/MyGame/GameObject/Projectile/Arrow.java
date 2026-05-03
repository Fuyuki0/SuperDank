package MyGame.GameObject.Projectile;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.Game.World;

import java.util.ArrayList;
import java.util.List;

import static MyGame.Game.Main.SCREEN_HEIGHT;
import static MyGame.Game.Main.SCREEN_WIDTH;

public class Arrow extends Projectile {
    private double speed = 1600;
    private double velocityX;
    private double velocityY;
    private double faceToX;
    private double faceToY;
    private double angle;

    private int pierceCount = 4;
    private double fadeTimer;
    private List<Enemy> hitEnemies;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Arrow(double posX, double posY, double enemiesPosX, double enemiesPosY) {
        super(posX, posY, 10.0);
        this.fadeTimer = 8.0;
        this.hitEnemies = new ArrayList<>();
        double directionX = enemiesPosX - posX;
        double directionY = enemiesPosY - posY;
        double direction = Math.sqrt(directionX * directionX + directionY * directionY);
        if (direction > 0) {
            this.velocityX = directionX / direction;
            this.velocityY = directionY / direction;
            setFaceTo(velocityX, velocityY);
            this.angle = Math.toDegrees(Math.atan2(this.velocityY, this.velocityX));
        }
    }

    public void setFaceTo(double velocityX, double velocityY) {
        if (this.velocityX != 0 || this.velocityY != 0) {
            this.faceToX = velocityX;
            this.faceToY = velocityY;
        }
        this.angle = Math.toDegrees(Math.atan2(this.faceToY, this.faceToX));
    }

    public void checkBound(double cameraPosX, double cameraPosY, double screenWidth, double screenHeight) {
        if (this.posX < cameraPosX                && this.velocityX < 0) {
            this.posX = cameraPosX                ; this.velocityX *= -1 ; hitEnemies.clear();}
        if (this.posX > cameraPosX + screenWidth  && this.velocityX > 0) {
            this.posX = cameraPosX + screenWidth  ; this.velocityX *= -1 ; hitEnemies.clear();}
        if (this.posY < cameraPosY                && this.velocityY < 0) {
            this.posY = cameraPosY                ; this.velocityY *= -1 ; hitEnemies.clear();}
        if (this.posY > cameraPosY + screenHeight && this.velocityY > 0) {
            this.posY = cameraPosY + screenHeight ; this.velocityY *= -1 ; hitEnemies.clear();}
        setFaceTo(velocityX, velocityY);
    }

    public boolean canPierce() {
        return pierceCount > 0;
    }

    public boolean hitEnemy(Enemy enemy) {
        if (!hitEnemies.contains(enemy)) {
            hitEnemies.add(enemy);
            pierceCount--;
            return true;
        } else return false;
    }

    @Override
    public void update(double deltaTime) {
        this.posX += velocityX * speed * deltaTime;
        this.posY += velocityY * speed * deltaTime;
        this.fadeTimer -= deltaTime;
        updateAnimation(deltaTime);
    }

    public void updateProj(double deltaTime, World world) {
        this.update(deltaTime);
        double cameraPosX = world.getPlayer().getPosX() - (SCREEN_WIDTH / 2.0);
        double cameraPosY = world.getPlayer().getPosY() - (SCREEN_HEIGHT / 2.0);
        this.checkBound(cameraPosX, cameraPosY, SCREEN_WIDTH, SCREEN_HEIGHT);

        for (int j = 0; j < world.getEnemies().size(); j++) {
            Enemy enemy = world.getEnemies().get(j);
            if (this.checkCollision(enemy)) {
                if (this.hitEnemy(enemy)) {
                    double arrowDamage = 30 * (1 + world.getBow().getBonusDamage() / 100);
                    if (Math.random() < world.getBow().getBonusCritRate() / 100) {
                        arrowDamage *= (1 + world.getBow().getBonusCritDmg() / 100);
                        enemy.takeDamageAndEffectPlayer(world.getPlayer(), arrowDamage, world.getDamageTexts(), true);
                    } else {
                        enemy.takeDamageAndEffectPlayer(world.getPlayer(), arrowDamage, world.getDamageTexts(), false);
                    }
                    enemy.smoothHitboxContactPushOut(this.getFaceToX() * 30, this.getFaceToY() * 30, 0.08);
                }
            }
        }
    }

    public boolean isDone() {
        return !this.canPierce() || this.isFade();
    }

    public void updateAnimation(double deltaTime) {
        animationTimer += deltaTime;
        if (animationTimer > 0.1) {
            currentFrame++;
            if (currentFrame >= 30) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    public boolean isFade() {
        return this.fadeTimer <= 0;
    }

    public double opacity() {
        if (this.fadeTimer > 0.5) {
            return 1.0;
        }return Math.max(0, this.fadeTimer / 0.5);
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 64;
        double frameHeight = 64;
        double scaleArrow = 1.1;
        gc.save();
        gc.setGlobalAlpha(this.opacity());
        double sourceX = 0 + this.getCurrentFrame() * frameWidth;
        double sourceY = 0;
        gc.translate(this.getPosX() - cameraPosX, this.getPosY() - cameraPosY);
        gc.rotate(this.getAngle());
        gc.drawImage(
                engine.getArrowImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth * scaleArrow / 2), -(frameHeight * scaleArrow / 2), frameWidth * scaleArrow, frameHeight * scaleArrow
        );
        gc.restore();
        gc.setGlobalAlpha(1.0);
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

    public int getPierceCount() {
        return pierceCount;
    }

    public void setPierceCount(int pierceCount) {
        this.pierceCount = pierceCount;
    }

    public double getFadeTimer() {
        return fadeTimer;
    }

    public void setFadeTimer(double fadeTimer) {
        this.fadeTimer = fadeTimer;
    }

    public List<Enemy> getHitEnemies() {
        return hitEnemies;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setHitEnemies(List<Enemy> hitEnemies) {
        this.hitEnemies = hitEnemies;
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

