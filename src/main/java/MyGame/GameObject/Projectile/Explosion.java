package MyGame.GameObject.Projectile;

import MyGame.Game.GameEngine;
import MyGame.GameObject.GameObject;

public class Explosion extends GameObject {
    private final double MAX_TIMER = 0.4;
    private double timer;

    public Explosion(double posX, double posY) {
        super(posX, posY);
        this.timer = MAX_TIMER;
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 64;
        double frameHeight = 64;
        double scaleExplosion = 4;
        double sourceX = engine.getWorld().getCurrentExplosionFrame() * frameWidth;
        double sourceY = 0;
        gc.save();
        gc.setGlobalAlpha(this.opacity());
        gc.translate(this.getPosX() - cameraPosX, this.getPosY() - cameraPosY);
        gc.drawImage(
                engine.getExplosionImage(),
                sourceX, sourceY, frameWidth, frameHeight,
                -(frameWidth / 2) * scaleExplosion, -(frameHeight / 2) * scaleExplosion, frameWidth * scaleExplosion, frameHeight * scaleExplosion
        );
        gc.restore();
    }

    @Override
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