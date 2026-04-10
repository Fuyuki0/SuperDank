package MyGame.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Experience extends GameObject{
    private int expValue;
    private int expType;

    private static final List<Experience> pool = new ArrayList<>();

    public static Experience create(double posX, double posY) {
        if (pool.isEmpty()) return new Experience(posX, posY);
        Experience exp = pool.remove(pool.size() - 1);
        exp.init(posX, posY);
        return exp;
    }

    public static void release(Experience experience) {
        pool.add(experience);
    }

    public Experience(double posX, double posY) {
        super(posX, posY, 40);
        init(posX, posY);
    }

    public void init(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        double check = Math.random() * 100;
        double expGain = 5;
        if (check <= 60) {
            this.expType = 0;
            this.expValue = (int)expGain;
        } else if (check <= 88) {
            this.expType = 1;
            this.expValue = (int)expGain * 3;
        } else if (check <= 98) {
            this.expType = 2;
            this.expValue = (int)expGain * 6;
        } else if (check <= 100) {
            this.expType = 3;
            this.expValue = (int)expGain * 20;
        }
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public boolean checkCollision(GameObject object) {
        return super.checkCollision(object);
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }

    public int getExpType() {
        return expType;
    }

    public void setExpType(int expType) {
        this.expType = expType;
    }

}
