import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Player;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Enemies.Charger;
import MyGame.GameObject.Enemies.Boss;

public class EntityTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0); // Fresh player before every test
    }

    @Test
    public void testPlayerHealthEncapsulation() {
        double maxHealth = player.getMaxHealth();

        // Try to heal over maximum
        player.setCurrentHealth(maxHealth + 1000);
        assertEquals(maxHealth, player.getCurrentHealth(), "Health should be capped at maxHealth");

        // Try to take fatal damage
        player.setCurrentHealth(-50);
        assertEquals(0, player.getCurrentHealth(), "Health should not drop below 0");
    }

    @Test
    public void testEnemyInheritanceIdentity() {
        Enemy normal = new Enemy(100, 100, player);
        Enemy charger = new Charger(100, 100, player);
        Enemy boss = new Boss(100, 100, player);

        // Verify Polymorphic flags
        assertFalse(normal.isCharger(), "Normal enemy should not be a charger");
        assertFalse(normal.isBoss(), "Normal enemy should not be a boss");

        assertTrue(charger.isCharger(), "Charger must return true for isCharger()");
        assertTrue(boss.isBoss(), "Boss must return true for isBoss()");
    }

    @Test
    public void testDistanceCollisionMath() {
        player.setPosX(0);
        player.setPosY(0);
        player.setHitbox(20);

        Enemy enemy = new Enemy(15, 0, player);
        enemy.setHitbox(20);

        // Combined hitbox radius is 40. Distance is 15. They should collide!
        assertTrue(player.checkCollision(enemy), "Entities should collide when overlapping");

        enemy.setPosX(100);
        // Distance is now 100. They should not collide!
        assertFalse(player.checkCollision(enemy), "Entities should not collide from far away");
    }
}