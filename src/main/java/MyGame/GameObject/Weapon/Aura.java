package MyGame.GameObject.Weapon;

import MyGame.GameObject.SoundManager;
import MyGame.World;

public class Aura extends Weapon{
    private double radius;
    private double timer;
    private final double TICK_RATE = 0.5;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Aura() {
        super(WeaponType.AURA);
        this.radius = 250;
        this.timer = 0;
    }

    public void update(double deltaTime, World world) {
        this.attackWhileJumping = false;
        if (timer > 0) {
            timer -= deltaTime;
        }

        animationTimer += deltaTime;
        if (animationTimer > TICK_RATE * Math.max(0.1, 1 - (getBonusAttackSpeed() + world.getPlayer().getStatAtkSpeed()) / 100 ) / 6) {
            currentFrame++;
            if (currentFrame >= 6) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    public boolean inArea(World world) {
        if (timer <= 0 ) {
            double totalAtkSpeed = getBonusAttackSpeed() + world.getPlayer().getStatAtkSpeed();
            timer = TICK_RATE * Math.max(0.1, 1 - totalAtkSpeed / 100 );
            SoundManager.auraSound.play();
            return true;
        }
        return false;
    }

    @Override
    public String[] getWeaponStat() {
        return new String[] {"Damage", "Atk Speed", "Size"};
    }

    public void playerAttack(World world) {

    }

    public double getTICK_RATE() {
        return TICK_RATE;
    }

    @Override
    public double getTimer() {
        return timer;
    }

    @Override
    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
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
        return "Aura";
    }
}
