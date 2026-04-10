package MyGame.GameObject;

import java.util.Comparator;
import java.util.Objects;

public abstract class GameObject {
    protected double posX;
    protected double posY;
    protected double hitbox;

    public GameObject() {
    }

    public GameObject(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public GameObject(double posX, double posY, double hitbox) {
        this.posX = posX;
        this.posY = posY;
        this.hitbox = hitbox;
    }

    public void hitboxContactPushOut(double distanceX, double distanceY) {
        this.posX += distanceX;
        this.posY += distanceY;
    }



    public boolean checkCollision(GameObject object) {
        double distanceX = this.getPosX() - object.getPosX();
        double distanceY = this.getPosY() - object.getPosY();
        double distance = distanceX * distanceX + distanceY * distanceY;
        double radius = this.getHitbox() + object.getHitbox();
        return distance < radius * radius;
    }

    public void push(GameObject object) {
        double distanceX = this.getPosX() - object.getPosX();
        double distanceY = this.getPosY() - object.getPosY();
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        if (distance <= 0.0) {
            this.hitboxContactPushOut(  Math.random() - 0.5, Math.random() - 0.5);
            object.hitboxContactPushOut(Math.random() - 0.5, Math.random() - 0.5);
            return;
        }
        double overlap = this.getHitbox() + object.getHitbox() - distance;
        double pushX = (distanceX / distance) * ((overlap / 2.0) + 0.1);
        double pushY = (distanceY / distance) * ((overlap / 2.0) + 0.1);

        this.pushObject(object, pushX, pushY);
    }

    protected void pushObject(GameObject object, double pushX, double pushY) {
        this.hitboxContactPushOut(pushX, pushY);
        object.hitboxContactPushOut(-pushX, -pushY);
    }
    protected abstract void update(double deltaTime);

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double PosX) {
        this.posX = PosX;
    }

    public double getHitbox() {
        return hitbox;
    }

    public void setHitbox(double hitbox) {
        this.hitbox = hitbox;
    }
}
