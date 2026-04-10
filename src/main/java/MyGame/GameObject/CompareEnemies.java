package MyGame.GameObject;

import MyGame.GameObject.Enemies.Enemy;

import java.util.Comparator;
import java.util.List;

public class CompareEnemies {
    private GameObject object_1;
    private List<Enemy> object_2;
    private final Comparator<GameObject> objectComparator = Comparator.comparingDouble(object_2 -> {
        double distanceX = object_2.getPosX() - object_1.getPosX();
        double distanceY = object_2.getPosY() - object_1.getPosY();
        return distanceX * distanceX + distanceY * distanceY;
    });

    public CompareEnemies(GameObject object_1, List<Enemy> object_2) {
        this.object_1 = object_1;
        this.object_2 = object_2;
    }

    public void useComparator(List<Enemy> objects) {
        objects.sort(objectComparator);
    }

    public List<Enemy> getObject_2() {
        return object_2;
    }

    public void setObject_2(List<Enemy> object_2) {
        this.object_2 = object_2;
    }
}
