package MyGame.GameObject.Projectile;

import MyGame.Game.GameEngine;
import MyGame.Game.World;

import java.util.List;

public class LightningEffect extends Projectile {
    private List<double[]> strikePoints;
    private double timer;
    private final double MAX_TIMER = 0.3; // Fades out in 0.3 seconds

    private int currentFrame = 0;
    private int currentFrame_2 = 0;
    private double animationTimer = 0.0;
    private double animationTimer2 = 0.0;

    public LightningEffect(List<double[]> strikePoints) {
        super(strikePoints);
        this.strikePoints = strikePoints;
        this.timer = MAX_TIMER;
    }

    public void update(double deltaTime) {
        this.timer -= deltaTime;

        animationTimer += deltaTime;
        if (animationTimer > 0.07) {
            currentFrame++;
            if (currentFrame >= 4) {
                currentFrame = 0;
            }
            animationTimer = 0;
        }
        animationTimer2 += deltaTime;
        if (animationTimer2 > MAX_TIMER / 30){
            currentFrame_2++;
            if (currentFrame_2 >= 30) {
                currentFrame_2 = 0;
            }
            animationTimer2 = 0;
        }
    }

    @Override
    public void updateProj(double deltaTime, World world) {
        this.update(deltaTime);
    }

    @Override
    public boolean isDone() {
        return this.isFade();
    }

    public boolean isFade() {
        return timer <= 0;
    }

    public double opacity() {
        return Math.max(0, timer / MAX_TIMER);
    }

    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        double frameWidth = 128;
        double frameHeight = 128;
        double sourceY = 0;
        double sourceX = this.getCurrentFrame() * frameWidth;
        gc.save();
        gc.setGlobalAlpha(this.opacity());
        for (int i = 0; i < this.getStrikePoints().size() - 1; i++) {
            double[] point1 = this.getStrikePoints().get(i);
            double[] point2 = this.getStrikePoints().get(i + 1);
            double startX = point1[0] - cameraPosX;
            double startY = point1[1] - cameraPosY;
            double endX = point2[0] - cameraPosX;
            double endY = point2[1] - cameraPosY;
            double distanceX = endX - startX;
            double distanceY = endY - startY;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            double angle = Math.toDegrees(Math.atan2(distanceY, distanceX));
            gc.save();
            gc.translate(startX, startY);
            gc.rotate(angle);
            gc.drawImage(
                    engine.getLightningImage(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    0, -(frameHeight / 2.0), distance, frameHeight
            );
            gc.restore();
            frameWidth = 96;
            frameHeight = 96;
            double scaleLightning = 1;
            sourceX = this.getCurrentFrame_2() * frameWidth;
            sourceY = 0;
            gc.save();
            gc.translate(endX, endY);
            gc.drawImage(
                    engine.getLightningImage2(),
                    sourceX, sourceY, frameWidth, frameHeight,
                    -(frameWidth * scaleLightning / 2.0), -(frameHeight *scaleLightning / 2.0), frameWidth * scaleLightning, frameHeight * scaleLightning
            );
            gc.restore();
            }
        gc.restore();
        gc.setGlobalAlpha(1);
    }


    public void setStrikePoints(List<double[]> strikePoints) {
        this.strikePoints = strikePoints;
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

    public List<double[]> getStrikePoints() {
        return strikePoints;
    }

    public int getCurrentFrame_2() {
        return currentFrame_2;
    }

    public void setCurrentFrame_2(int currentFrame_2) {
        this.currentFrame_2 = currentFrame_2;
    }
}