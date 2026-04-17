package MyGame.GameObject.Projectile;

import MyGame.GameEngine;
import MyGame.GameObject.GameObject;
import MyGame.World;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public abstract class Projectile extends GameObject {

    public Projectile(double posX, double posY, double hitbox) {
        super(posX, posY, hitbox);
    }

    public Projectile(List<double[]> strikePoints) {}

    public Projectile(double angle) {}

    public Projectile(double posX, double posY) {
        super(posX, posY);
    }

    public abstract void updateProj(double deltaTime, World world);
    public abstract boolean isDone();
    public abstract void draw(GraphicsContext gc, double cameraPosX, double cameraPosY, GameEngine engine);
}