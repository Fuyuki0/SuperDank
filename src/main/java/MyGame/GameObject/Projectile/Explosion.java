package MyGame.GameObject.Projectile;

public class Explosion {
    private double posX, posY;
    private double timer;
    private final double MAX_TIMER = 0.4;

    public Explosion(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.timer = MAX_TIMER;
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;
    }

    public boolean isFade() {
        return timer <= 0;
    }

    public double opacity() {
        return Math.max(0, timer / MAX_TIMER);
    }

    public double getRadius() {
        return 200 * (1 - opacity()) + 50;
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

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getMAX_TIMER() {
        return MAX_TIMER;
    }
}