package MyGame.GameObject.Enemies;

import MyGame.Game.GameEngine;
import MyGame.GameObject.DamageText;
import MyGame.GameObject.GameObject;
import MyGame.GameObject.Player.Player;
import MyGame.Game.SoundManager;
import javafx.scene.paint.Color;

import java.util.List;

public class Enemy extends GameObject {
    protected Player player = null;
    protected double speed;
    protected double faceToX;
    protected double faceToY;
    protected double directionX;
    protected double directionY;

    protected double damageTimer;
    protected double knockBackTimer;
    protected double health;
    protected double maxHealth;

    protected double knockBackVelocityX = 0;
    protected double knockBackVelocityY = 0;

    // type
    protected boolean isCharger = false;
    protected boolean isBoss = false;


    // animation
    private int currentFrame = 0;
    private double animationTimer = 0.0;
    private int animationState = 0;
    protected double flashTimer = 0;


    public Enemy(double posX, double posY, Player player) {
        super(posX, posY, 20);
        this.posX = posX;
        this.posY = posY;
        this.player = player;
        this.speed = 250.0;
        this.damageTimer = 0;
        this.knockBackTimer = 0;
        this.maxHealth = 120;
        this.health = maxHealth;
        this.knockBackVelocityX = 0;
        this.knockBackVelocityY = 0;
        this.isCharger = false;
        this.isBoss = false;
    }

    public void doDamage(Player player, double amount) {
        if (player.isShielded()) return;
        if (this.damageTimer <= 0) {
            if (player.getCurrentOverHeal() > 0) {
                double overHeal = player.getCurrentOverHeal();
                double overDamage = 0;
                if (amount > overHeal) {
                    overDamage = amount - overHeal;
                    player.setCurrentOverHeal(0);
                    player.setCurrentHealth(player.getCurrentHealth() - overDamage);
                } else {
                    overDamage = overHeal - amount;
                    player.setCurrentOverHeal(overDamage);
                }
                if (player.getCurrentOverHeal() < 0) player.setCurrentOverHeal(0);
            } else {
                double health = player.getCurrentHealth();
                player.setCurrentHealth(health - amount);
                if (player.getCurrentHealth() < 0) player.setCurrentHealth(0);
            }
            player.setFlashTimer(0.05);
            this.damageTimer = 0.2;
        }
    }

    public void takeDamageAndEffectPlayer(Player player, double amount, List<DamageText> damageTexts, boolean isCrit) {

        // item effect
        if (!this.isBoss && Math.random() < player.getInstantKillChance()) {
            amount = this.health + player.getCurrentOverHeal();
        }
        if (isCrit && player.isHasDeepestVoid() && Math.random() < 0.2) {
            player.getPendingBlackHoles().add(new double[]{this.posX, this.posY, 3.0, 0.0});
        }

        this.setHealth(this.health - amount);
        if (SoundManager.hitSound != null && !SoundManager.hitSound.isPlaying()) {
            SoundManager.hitSound.play();
        }

        damageTexts.add(new DamageText(this.posX, this.posY, amount, isCrit));

        if (player.getCurrentHealth() == player.getMaxHealth()) {
            if (player.hasMaxOverHeal() && player.getCurrentOverHeal() != player.getMaxOverHeal()) {
                if (this.health >= amount)
                    player.setCurrentOverHeal(player.getCurrentOverHeal() + amount * player.getLifeSteal());
                else
                    player.setCurrentOverHeal(player.getCurrentOverHeal() + (amount - this.health) * player.getLifeSteal());
            }
        } else {
            if (this.health >= amount)
                player.setCurrentHealth(player.getCurrentHealth() + amount * player.getLifeSteal());
            else
                player.setCurrentHealth(player.getCurrentHealth() + (amount - this.health) * player.getLifeSteal());
        }

        this.flashTimer = 0.1;
    }

    public boolean isDead() {
        this.setHealth(health);
        return health <= 0;
    }

    public void smoothHitboxContactPushOut(double distanceX, double distanceY, double knockBackTimer) {
        this.knockBackTimer = knockBackTimer;
        if (isBoss) {
            this.knockBackVelocityX = distanceX / knockBackTimer / 4;
            this.knockBackVelocityY = distanceY / knockBackTimer / 4;
        } else {
            this.knockBackVelocityX = distanceX / knockBackTimer;
            this.knockBackVelocityY = distanceY / knockBackTimer;
        }
    }

    public void updateKnockBack(double deltaTime) {
        if (this.knockBackTimer > 0) {
            this.posX += this.knockBackVelocityX * deltaTime;
            this.posY += this.knockBackVelocityY * deltaTime;
            this.knockBackVelocityX *= 0.9;
            this.knockBackVelocityY *= 0.9;
            if (Math.abs(knockBackVelocityX) < 1.0) knockBackVelocityX = 0;
            if (Math.abs(knockBackVelocityY) < 1.0) knockBackVelocityY = 0;

            this.knockBackTimer -= deltaTime;
            if (this.knockBackTimer < 0) {
                this.knockBackTimer = 0;
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        double distanceX = this.player.getPosX() - this.posX;
        double distanceY = this.player.getPosY() - this.posY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        if (!this.checkCollision(this.player)) {
            this.posX += (distanceX / distance) * speed * deltaTime;
            this.posY += (distanceY / distance) * speed * deltaTime;
        }
        this.faceToX = distanceX / distance;
        this.faceToY = distanceY / distance;

        if (this.damageTimer > 0) {
            this.damageTimer -= deltaTime;
        }
        updateKnockBack(deltaTime);

        updateAnimation(deltaTime);

        if (flashTimer > 0) {
            flashTimer -= deltaTime;
        }
    }

    public void updateAnimation(double deltaTime) {
        animationTimer += deltaTime;
        if (isCharger) {
            if (animationState != 0) {
                animationState = 0;
                currentFrame = 0;
            }
            if (animationTimer > 0.3) {
                currentFrame++;
                if (currentFrame >= 2) {
                    currentFrame = 0;
                }
                animationTimer = 0;
            }
        } else if (isBoss) {
            if (animationState != 1) {
                animationState = 1;
                currentFrame = 0;
            }
            if (animationTimer > 0.3) {
                currentFrame++;
                if (currentFrame >= 3) {
                    currentFrame = 0;
                }
                animationTimer = 0;
            }
        } else {
            if (animationState != 2) {
                animationState = 2;
                currentFrame = 0;
            }
            if (animationTimer > 0.1) {
                currentFrame++;
                if (currentFrame >= 4) {
                    currentFrame = 0;
                }
                animationTimer = 0;
            }
        }
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        javafx.scene.effect.Effect hitFlash = engine.getHitFlash();
        javafx.scene.image.Image enemyMech = engine.getEnemyMech();
        javafx.scene.image.Image chargerMech = engine.getChargerMech();
        javafx.scene.image.Image bossMech = engine.getBossMech();
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        double shadowWidth = 60;
        double shadowHeight = 20;
        gc.fillOval(this.getPosX() - cameraPosX - (shadowWidth / 2.0), this.getPosY() - cameraPosY - (shadowHeight / 2.0) + 40, shadowWidth, shadowHeight);

        double offsetX = 0;
        double offsetY = 0;
        double scaleEnemy = 1.3;
        double drawEnemyX = this.getPosX() - cameraPosX - offsetX;
        double drawEnemyY = this.getPosY() - cameraPosY - offsetY;
        double frameWidth = 58;
        double frameHeight = 77;
        double sourceX = 0;
        double sourceY = 0;

        if (this.getPosX() - cameraPosX > -margin
                && this.getPosX() - cameraPosX < screenWidth + margin
                && this.getPosY() - cameraPosY > -margin
                && this.getPosY() - cameraPosY < screenHeight + margin
        ) {
            double idleStartX = 0;
            double idleStartY = 0;

            if (this.getPosY() - cameraPosY < screenHeight / 2.0 + 40) {
                idleStartY = 0;
            } else {
                idleStartY = 77;
            }
            sourceX = idleStartX + (this.getCurrentFrame() * frameWidth);
            sourceY = idleStartY;
            boolean isFlipped = this.getPosX() - cameraPosX > screenWidth / 2.0;

            gc.save();
            gc.translate(drawEnemyX, drawEnemyY);

            // flash
            if (this.getFlashTimer() > 0) {
                gc.setEffect(hitFlash);
            }

            if (isFlipped) {
                gc.scale(-1, 1);
            }
            gc.drawImage(
                    enemyMech,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth * scaleEnemy / 2), -(frameHeight * scaleEnemy / 2), frameWidth * scaleEnemy, frameHeight * scaleEnemy
            );

            gc.setEffect(null);
            gc.restore();
        }
    }

    @Override
    protected void pushObject(GameObject object, double pushX, double pushY) {
        if (object instanceof Player) {
            this.hitboxContactPushOut(pushX * 3, pushY * 3);
        } else {
            super.pushObject(object, pushX, pushY);
        }
    }


    // s + g

    public void addMaxHealth(double amount) {
        this.maxHealth = this.maxHealth + amount;
    }

    public Player getPlayer() {
        return player;
    }

    public double getSpeed() {
        return speed;
    }

    public double getTimer() {
        return damageTimer;
    }

    public void setTimer(double timer) {
        this.damageTimer = timer;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        if (this.health < 0) this.health = 0;
        else if (this.health > this.maxHealth) this.health = maxHealth;
        else this.health = health;
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDamageTimer() {
        return damageTimer;
    }

    public void setDamageTimer(double damageTimer) {
        this.damageTimer = damageTimer;
    }

    public double getKnockBackTimer() {
        return knockBackTimer;
    }

    public void setKnockBackTimer(double knockBackTimer) {
        this.knockBackTimer = knockBackTimer;
    }

    public double getKnockBackVelocityX() {
        return knockBackVelocityX;
    }

    public void setKnockBackVelocityX(double knockBackVelocityX) {
        this.knockBackVelocityX = knockBackVelocityX;
    }

    public double getKnockBackVelocityY() {
        return knockBackVelocityY;
    }

    public void setKnockBackVelocityY(double knockBackVelocityY) {
        this.knockBackVelocityY = knockBackVelocityY;
    }

    public boolean isCharger() {
        return false;
    }

    public void setCharger(boolean charger) {
        isCharger = charger;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isBoss() {
        return false;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
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

    public int getAnimationState() {
        return animationState;
    }

    public void setAnimationState(int animationState) {
        this.animationState = animationState;
    }

    public double getFlashTimer() {
        return flashTimer;
    }

    public void setFlashTimer(double flashTimer) {
        this.flashTimer = flashTimer;
    }
}