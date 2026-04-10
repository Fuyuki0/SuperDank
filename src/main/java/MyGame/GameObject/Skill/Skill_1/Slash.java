package MyGame.GameObject.Skill.Skill_1;

public class Slash {
    private double posX;
    private double posY;
    private double posZ;
    private double angle;
    private double timer;
    private final double MAX_TIMER = 0.2;
    private boolean slashHit;

    private int currentFrame = 0;
    private double animationTimer = 0.0;

    public Slash(double posX, double posY, double posZ, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.angle = angle;
        this.timer = MAX_TIMER;
        this.slashHit = false;
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        this.animationTimer += deltaTime;
        if (animationTimer > 0.02) {
            currentFrame++;
            if (currentFrame >= 7) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
    }

    public boolean fade() {
        return timer <= 0;
    }

    public double opacity() {
        return Math.max(0, timer / MAX_TIMER);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getMAX_TIMER() {
        return MAX_TIMER;
    }

    public boolean isSlashHit() {
        return slashHit;
    }

    public void setSlashHit(boolean slashHit) {
        this.slashHit = slashHit;
    }

    public double getPosZ() {
        return posZ;
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

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }
}
