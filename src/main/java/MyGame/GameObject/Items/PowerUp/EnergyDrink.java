package MyGame.GameObject.Items.PowerUp;

import MyGame.GameObject.GameObject;

public class EnergyDrink extends GameObject {
    private int drinkType;

    public EnergyDrink(double posX, double posY) {
        super(posX, posY, 15);
        this.drinkType = (int) (Math.random() * 3);
    }
    @Override
    public void update(double deltaTime) { }

    public int getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(int drinkType) {
        this.drinkType = drinkType;
    }
}