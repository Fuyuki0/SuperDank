package MyGame.GameObject;

import MyGame.Game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle extends GameObject{
    private double width;
    private double height;

    public Obstacle(double posX, double posY, double width, double height) {
        super(posX, posY);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight, double margin, GameEngine engine) {
        gc.setFill(Color.rgb(50, 50, 50));
        gc.setStroke(Color.rgb(30, 30, 30));
        gc.setLineWidth(5);
        for (Obstacle obstacle : engine.getWorld().getObstacles()) {
            margin = 1000;
            if (obstacle.getPosX() - cameraPosX > -margin
                    && obstacle.getPosX() - cameraPosX < screenWidth + margin
                    && obstacle.getPosY() - cameraPosY > -margin
                    && obstacle.getPosY() - cameraPosY < screenHeight + margin
            ) {
                double obstaclePosX = obstacle.getPosX() - cameraPosX;
                double obstaclePosY = obstacle.getPosY() - cameraPosY;
                if (obstaclePosX + obstacle.getWidth() > 0 && obstaclePosX < screenWidth && obstaclePosY + obstacle.getHeight() > 0 && obstaclePosY < screenHeight) {
                    gc.fillRect(obstaclePosX, obstaclePosY, obstacle.getWidth(), obstacle.getHeight());
                    gc.strokeRect(obstaclePosX, obstaclePosY, obstacle.getWidth(), obstacle.getHeight());
                }
            }
        }
    }


    public void update(double deltaTime) {}

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
