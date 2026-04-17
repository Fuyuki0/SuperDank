package MyGame.GameObject.Weapon;

import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player;
import MyGame.GameObject.SoundManager;
import MyGame.World;

public class Sword extends Weapon {

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Sword() {
        super(1.5, 0.15, WeaponType.SWORD);
    }

    public boolean isAttacking() {
        return activeTimer > 0;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Size"};
    }

    @Override
    public void update(double deltaTime, World world) {
        super.update(deltaTime, world);

        animationTimer += deltaTime;
        if (isAttacking()) {
            if (animationTimer > activeDuration / 10) {
                currentFrame++;
                if (currentFrame >= 10) {
                    currentFrame = 0;
                }
                if (currentFrame < 0) {
                    currentFrame = 0;
                }
                animationTimer = 0;
            }
        }
    }

    @Override
    public void playerAttack(World world) {
        Player player = world.getPlayer();
        this.activeTimer = activeDuration;
        SoundManager.swordSound.play();
        double sizeMultiplier = 1 + (this.getBonusSize() / 100);
        double attackRangeA = 500 * sizeMultiplier;
        double attackRangeB = 200 * sizeMultiplier;
        double offset = 100 * sizeMultiplier;
        double centreX = player.getPosX() + (player.getFaceToX() * offset);
        double centreY = player.getPosY() + (player.getFaceToY() * offset);
        for (Enemy enemy : world.getEnemies()) {
            double distanceX = enemy.getPosX() - centreX;
            double distanceY = enemy.getPosY() - centreY;
            double distanceFaceX = (distanceX * player.getFaceToX()) + (distanceY * player.getFaceToY());
            double distanceFaceY = (-distanceX * player.getFaceToY()) + (distanceY * player.getFaceToX());
            double distance = Math.sqrt((distanceFaceX * distanceFaceX) / (attackRangeA * attackRangeA) + (distanceFaceY * distanceFaceY) / (attackRangeB * attackRangeB));
            if (distance <= 1) {

                double Damage = 60.0 * (1 + this.bonusDamage / 100.0);

                enemy.takeDamageAndEffectPlayer(player, Damage, world.getDamageTexts(), false);
                enemy.smoothHitboxContactPushOut(-enemy.getFaceToX() * 200, -enemy.getFaceToY() * 200, 0.08);

            }
        }
        this.currentFrame = 0;
        this.animationTimer = 0.0;
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

    public String getName() {
        return "Swinger";
    }
}
