package MyGame.GameObject.Skill;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player;
import MyGame.GameObject.SoundManager;
import MyGame.World;

public class Ultimate {
    private double ultimateTimer = 0;

    public Ultimate() {}

    public void update(Double deltaTime, World world) {
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
        } else {
            return;
        }
    }

    public double getUltimateTimer() {
        return ultimateTimer;
    }

    public void setUltimateTimer(double ultimateTimer) {
        this.ultimateTimer = ultimateTimer;
    }
}
