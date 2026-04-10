package MyGame.GameObject.Enemies;

import MyGame.GameObject.Player;
import MyGame.GameObject.Weapon.Cores.ChaosCore;

import java.util.ArrayList;
import java.util.List;

public class Charger extends Enemy {
    public Charger(double posX, double posY, Player player) {
        super(posX, posY, player);
    }

    @Override
    public void update(double deltaTime) {
        this.posX += directionX * speed * deltaTime;
        this.posY += directionY * speed * deltaTime;
        if (this.damageTimer > 0) {
            this.damageTimer -= deltaTime;
        }
        updateKnockBack(deltaTime);

        updateAnimation(deltaTime);

        if (flashTimer > 0) {
            flashTimer -= deltaTime;
        }
    }

    @Override
    public boolean isCharger() {
        return true;
    }

}
