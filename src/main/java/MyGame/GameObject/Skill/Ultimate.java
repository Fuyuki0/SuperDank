package MyGame.GameObject.Skill;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;
import MyGame.Game.SoundManager;
import MyGame.Interface.ActiveSkill;
import MyGame.Game.World;

public class Ultimate implements ActiveSkill {
    private double ultimateTimer = 0;

    public Ultimate() {}

    @Override
    public void updateSkill(double deltaTime, World world) {
        Player player = world.getPlayer();
        ultimateTimer += deltaTime;
        player.setJumping(false);
        player.setVelocityZ(0);
        if (ultimateTimer < 1.5) {
            player.setCurrentMovementState(Player.MovementState.UltimateUp);
        } else {
            player.setCurrentMovementState(Player.MovementState.UltimateDown);
        }
        if (world.getUltimateSoundCheck() < 1) {
            SoundManager.ultimatePlayer.play();
            world.setUltimateSoundCheck(world.getUltimateSoundCheck() + 1);
        }
        if (SoundManager.ultimatePlayer != null)
            SoundManager.ultimatePlayer.play();
        if (ultimateTimer < 1.5) {
            player.setPosZ((ultimateTimer / 1.5) * 3000);
        } else if (ultimateTimer < 1.95) {
            double slamProgress = (ultimateTimer - 1.5) / 0.42;
            player.setPosZ(5000 - (slamProgress * 5000));
        } else
            player.setPosZ(0);

        if (ultimateTimer < 2.0) {
            for (Enemy enemy : world.getEnemies()) {
                if (!enemy.isBoss()) {
                    double distanceX = player.getPosX() - enemy.getPosX();
                    double distanceY = player.getPosY() - enemy.getPosY();
                    double distance = distanceX * distanceX + distanceY * distanceY;
                    if (distance < 300 * 300) {
                        enemy.setPosX(enemy.getPosX() - distanceX * deltaTime * 4);
                        enemy.setPosY(enemy.getPosY() - distanceY * deltaTime * 4);
                    } else {
                        enemy.setPosX(enemy.getPosX() + distanceX * deltaTime * 4);
                        enemy.setPosY(enemy.getPosY() + distanceY * deltaTime * 4);
                    }
                }
            }
        } else if (ultimateTimer > 2.0 && (ultimateTimer - deltaTime) <= 2.0) {
            world.triggerScreenShake(0.5, 60);
            for (Enemy enemy : world.getEnemies()) {
                enemy.takeDamageAndEffectPlayer(player, 500 * (1 + player.getStatDamage() / 100), world.getDamageTexts(), false);
            }
        } else if (ultimateTimer >= 2.0 && ultimateTimer < 2.3) {
            world.setEnteringAnimationTimer(world.getEnteringAnimationTimer() - deltaTime);
            if (world.getEnteringAnimationTimer() > 0.06) {
                world.setCurrentEnteringFrame(world.getCurrentEnteringFrame() + 1);
                if (world.getCurrentEnteringFrame() >= 5) {
                    world.setCurrentEnteringFrame(0);
                }
                world.setEnteringAnimationTimer(0);
            }
        } else if (ultimateTimer >= 2.5 && ultimateTimer < 3) {
            world.setUltimateActive(false);
            player.setShielded(false);
        } else if (ultimateTimer >= 3 && ultimateTimer < 3.5){
            SoundManager.ultimatePlayer.stop();
            world.setUltimateSoundCheck(0);
        }
    }

    @Override
    public boolean isFinished() {
        return ultimateTimer >= 3.5;
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        World world = engine.getWorld();
        Player player = world.getPlayer();

        gc.save();
        gc.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(0, 0, screenWidth, screenHeight);
        if (ultimateTimer < 2.0) {
            gc.setStroke(javafx.scene.paint.Color.rgb(150, 0, 90, 0.4));
            gc.setLineWidth(15);
            double radius = 1200 - (ultimateTimer / 1.5) * 1200;
            gc.strokeOval(player.getPosX() - cameraPosX - radius, player.getPosY() - cameraPosY - radius, radius * 2, radius * 2);
        }

        if (ultimateTimer >= 2.0) {
            if (ultimateTimer < 2.1) {
                gc.setFill(javafx.scene.paint.Color.rgb(255, 255, 255, 0.9));
                gc.fillRect(0, 0, screenWidth, screenHeight);
            }
        }
        gc.restore();

        if (ultimateTimer > 2.0 && ultimateTimer < 2.3) {
            double frameWidth = 128 * 5;
            double frameHeight = 256 * 5;
            double sourceX = world.getCurrentEnteringFrame() * frameWidth;
            double sourceY = 0;
            double scaleEnter = 1;
            gc.save();
            gc.setGlobalAlpha(0.3);
            gc.translate(player.getPosX() - cameraPosX, player.getPosY() - cameraPosY - 500);
            gc.drawImage(
                    engine.getEnteringImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth / 2) * scaleEnter, -(frameHeight / 2) * scaleEnter, frameWidth * scaleEnter, frameHeight * scaleEnter
            );
            gc.restore();
        }
        gc.setGlobalAlpha(1);
    }

    public double getUltimateTimer() {
        return ultimateTimer;
    }

    public void setUltimateTimer(double ultimateTimer) {
        this.ultimateTimer = ultimateTimer;
    }
}
