package MyGame.GameObject;

import MyGame.Game.GameEngine;
import MyGame.GameObject.Player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents an obstacle in the game world that entities might collide with.
 */
public class Obstacle extends GameObject {
    private double width;
    private double height;

    public Obstacle(double posX, double posY, double width, double height) {
        super(posX, posY);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight,
            double margin, GameEngine engine) {

        double obstaclePosX = this.posX - cameraPosX;
        double obstaclePosY = this.posY - cameraPosY;

        double buildHeight = 500;

        // Dynamic transparency
        double alphaMult = 1.0;
        Player player = engine.getWorld().getPlayer();
        if (player != null) {
            if (player.getPosX() > this.posX - 50 && player.getPosX() < this.posX + width + 50 &&
                    player.getPosY() > this.posY - buildHeight - 50 && player.getPosY() < this.posY + height + 50) {
                alphaMult = 0.40;
            }
        }

        gc.setFill(Color.rgb(20, 20, 20, 1));
        gc.fillRect(obstaclePosX, obstaclePosY, width, height);

        gc.setStroke(Color.rgb(40, 40, 40, 0.3 * alphaMult));
        gc.setLineWidth(3);
        gc.strokeLine(obstaclePosX, obstaclePosY, obstaclePosX, obstaclePosY - buildHeight);
        gc.strokeLine(obstaclePosX + width, obstaclePosY, obstaclePosX + width, obstaclePosY - buildHeight);
        gc.strokeLine(obstaclePosX, obstaclePosY + height, obstaclePosX, obstaclePosY + height - buildHeight);
        gc.strokeLine(obstaclePosX + width, obstaclePosY + height, obstaclePosX + width,
                obstaclePosY + height - buildHeight);
    }


    public void drawHeight(GraphicsContext gc, double cameraPosX, double cameraPosY, double screenWidth, double screenHeight,
                     double margin, GameEngine engine) {

        double obstaclePosX = this.posX - cameraPosX;
        double obstaclePosY = this.posY - cameraPosY;

        double buildHeight = 800;

        // Dynamic transparency
        double alphaMult = 1.0;
        Player player = engine.getWorld().getPlayer();
        if (player != null) {
            double radar = 200;
            if (player.getPosX() > this.posX - radar && player.getPosX() < this.posX + width + radar &&
                    player.getPosY() > this.posY - buildHeight - radar && player.getPosY() < this.posY + height + radar) {
                alphaMult = 0.50;
            }
        }

        gc.setFill(Color.rgb(60, 60, 70, 0.7 * alphaMult));
        gc.fillRect(obstaclePosX, obstaclePosY - buildHeight, width, height);

        gc.setFill(Color.rgb(30, 40, 40, 0.6 * alphaMult));
        gc.fillRect(obstaclePosX, obstaclePosY - buildHeight, width, buildHeight + height);

        gc.setStroke(Color.rgb(30, 30, 40, 0.6 * alphaMult));
        gc.setLineWidth(4);
        gc.strokeRect(obstaclePosX, obstaclePosY - buildHeight, width, height);
    }

    public void update(double deltaTime) {
    }

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
