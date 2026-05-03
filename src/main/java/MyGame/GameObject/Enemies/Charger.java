package MyGame.GameObject.Enemies;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Player.Player;
import javafx.scene.paint.Color;

public class Charger extends Enemy {
    public Charger(double posX, double posY, Player player) {
        super(posX, posY, player);
        this.speed = 600;
        this.maxHealth = 80;
        this.health = maxHealth;
        this.isCharger = true;
    }

    @Override
    public void update(double deltaTime) {
        this.posX += directionX * speed * deltaTime;
        this.posY += directionY * speed * deltaTime;
        if (this.damageTimer > 0) {
            this.damageTimer -= deltaTime;
        }
        updateKnockBack(deltaTime);

        updateAnimation(deltaTime);

        if (flashTimer > 0) {
            flashTimer -= deltaTime;
        }
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        javafx.scene.effect.Effect hitFlash = engine.getHitFlash();
        javafx.scene.image.Image enemyMech = engine.getEnemyMech();
        javafx.scene.image.Image chargerMech = engine.getChargerMech();
        javafx.scene.image.Image bossMech = engine.getBossMech();
        
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        double shadowWidth = 40;
        double shadowHeight = 15;
        gc.fillOval(this.getPosX() - cameraPosX - (shadowWidth / 2.0), this.getPosY() - cameraPosY - (shadowHeight / 2.0) + 30, shadowWidth, shadowHeight);

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
            frameWidth = 60;
            frameHeight = 60;
            if (this.getDirectionY() > 0) {
                idleStartY = 0;
            } else {
                idleStartY = 60;
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

            if (this.getDirectionX() < 0) {
                gc.scale(-1, 1);
            }
            gc.drawImage(
                    chargerMech,
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth * scaleEnemy / 2), -(frameHeight * scaleEnemy / 2), frameWidth * scaleEnemy, frameHeight * scaleEnemy
            );
            gc.setEffect(null);
            gc.restore();
        }
    }

    @Override
    public boolean isCharger() {
        return true;
    }

}
