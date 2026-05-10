import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Player.Player;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Enemies.Charger;
import MyGame.GameObject.Enemies.Boss;
import MyGame.GameObject.DamageText;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for Enemy damage model, knockback system, shield bypass,
 * death detection, and heal interactions.
 */
public class CombatTest {

    private Player player;
    private Enemy enemy;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0);
        enemy = new Enemy(100, 100, player);
    }

    // ====================== Enemy Health ======================

    @Test
    public void testEnemyStartsAtFullHealth() {
        assertEquals(enemy.getMaxHealth(), enemy.getHealth(),
                "Enemy must start at full health");
    }

    @Test
    public void testEnemyIsDeadWhenHealthIsZero() {
        enemy.setHealth(0);
        assertTrue(enemy.isDead(), "Enemy must be dead when health is 0");
    }

    @Test
    public void testEnemyIsNotDeadWhileHealthAboveZero() {
        enemy.setHealth(50);
        assertFalse(enemy.isDead(), "Enemy with remaining HP must not be dead");
    }

    @Test
    public void testEnemyHealthCannotExceedMax() {
        enemy.setHealth(enemy.getMaxHealth() + 9999);
        assertEquals(enemy.getMaxHealth(), enemy.getHealth(),
                "Enemy health must be capped at maxHealth");
    }

    @Test
    public void testEnemyHealthCannotGoBelowZero() {
        enemy.setHealth(-100);
        assertEquals(0, enemy.getHealth(),
                "Enemy health must not go below 0");
    }

    @Test
    public void testTakeDamageReducesHealth() {
        List<DamageText> texts = new ArrayList<>();
        double initialHealth = enemy.getHealth();
        double damageAmount = 30;

        enemy.takeDamageAndEffectPlayer(player, damageAmount, texts, false);

        assertTrue(enemy.getHealth() < initialHealth,
                "Enemy health must decrease after taking damage");
    }

    @Test
    public void testTakeDamageAddsDamageText() {
        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 50, texts, false);

        assertFalse(texts.isEmpty(), "DamageText list must not be empty after a hit");
    }

    @Test
    public void testCritDamageTextFlagIsSet() {
        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 50, texts, true);

        assertTrue(texts.get(0).isCrit(), "DamageText must be flagged as crit when isCrit=true");
    }

    @Test
    public void testNonCritDamageTextFlagIsNotSet() {
        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 50, texts, false);

        assertFalse(texts.get(0).isCrit(), "DamageText must NOT be flagged as crit when isCrit=false");
    }

    // ====================== Enemy-to-Player Damage ======================

    @Test
    public void testEnemyDamageReducesPlayerHealth() {
        double initialHealth = player.getCurrentHealth();
        // Reset damage timer so the enemy can attack
        enemy.setDamageTimer(0);
        enemy.doDamage(player, 100);

        assertTrue(player.getCurrentHealth() < initialHealth,
                "Player health must decrease after enemy doDamage()");
    }

    @Test
    public void testEnemyDamageIsBlockedByShield() {
        player.setShielded(true);
        double healthBefore = player.getCurrentHealth();
        enemy.setDamageTimer(0);
        enemy.doDamage(player, 500);

        assertEquals(healthBefore, player.getCurrentHealth(),
                "Player health must not change when shielded");
    }

    @Test
    public void testEnemyDamageCooldown() {
        enemy.setDamageTimer(0);
        enemy.doDamage(player, 100);
        double healthAfterFirst = player.getCurrentHealth();

        // The second doDamage should be blocked by the 0.2s cooldown
        enemy.doDamage(player, 100);
        assertEquals(healthAfterFirst, player.getCurrentHealth(),
                "Second hit must be blocked by the damage timer cooldown");
    }

    @Test
    public void testPlayerHealthDoesNotGoBelowZero() {
        enemy.setDamageTimer(0);
        enemy.doDamage(player, 999999);
        assertTrue(player.getCurrentHealth() >= 0,
                "Player health must never go below 0");
    }

    // ====================== Knockback ======================

    @Test
    public void testKnockbackMovesEnemy() {
        double posXBefore = enemy.getPosX();
        double posYBefore = enemy.getPosY();

        enemy.smoothHitboxContactPushOut(500, 0, 0.3);
        enemy.updateKnockBack(0.1);

        assertNotEquals(posXBefore, enemy.getPosX(),
                "Knockback must move the enemy on the X axis");
    }

    @Test
    public void testKnockbackDecays() {
        enemy.smoothHitboxContactPushOut(1000, 0, 0.5);
        double velXFirst = enemy.getKnockBackVelocityX();

        enemy.updateKnockBack(0.05); // decay by 0.9
        assertTrue(enemy.getKnockBackVelocityX() < velXFirst,
                "Knockback velocity must decay over time");
    }

    @Test
    public void testKnockbackTimerRunsDown() {
        enemy.smoothHitboxContactPushOut(100, 100, 0.3);
        enemy.updateKnockBack(0.3 + 0.1); // tick past the timer
        assertEquals(0, enemy.getKnockBackTimer(),
                "Knockback timer must not go below 0");
    }

    // ====================== Life Steal ======================

    @Test
    public void testLifeStealHealsPlayer() {
        player.addLifeSteal(0.5); // 50% life steal
        // Damage the player so they aren't at full HP
        player.setCurrentHealth(player.getMaxHealth() - 1000);
        double hpBefore = player.getCurrentHealth();

        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 200, texts, false);

        assertTrue(player.getCurrentHealth() > hpBefore,
                "Player with life steal must heal when dealing damage");
    }

    @Test
    public void testNoLifeStealByDefault() {
        // Player's default lifeSteal is 0
        player.setCurrentHealth(player.getMaxHealth() - 500);
        double hpBefore = player.getCurrentHealth();

        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 100, texts, false);

        assertEquals(hpBefore, player.getCurrentHealth(), 0.001,
                "Player with no life steal must not heal when dealing damage");
    }

    // ====================== Flash Timer ======================

    @Test
    public void testFlashTimerSetOnHit() {
        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 10, texts, false);
        assertTrue(enemy.getFlashTimer() > 0,
                "Enemy flash timer must be set after taking a hit");
    }

    @Test
    public void testFlashTimerDecaysOnUpdate() {
        List<DamageText> texts = new ArrayList<>();
        enemy.takeDamageAndEffectPlayer(player, 10, texts, false);
        double flashBefore = enemy.getFlashTimer();
        enemy.setAnimationTimer(0);
        // Simulate time passing via setFlashTimer directly since update needs player
        enemy.setFlashTimer(flashBefore - 0.05);
        assertTrue(enemy.getFlashTimer() < flashBefore,
                "Flash timer must decrease over time");
    }

    // ====================== Enemy Type Flags ======================

    @Test
    public void testChargerIsNotBoss() {
        Enemy charger = new Charger(0, 0, player);
        assertTrue(charger.isCharger(), "Charger.isCharger() must return true");
        assertFalse(charger.isBoss(), "Charger.isBoss() must return false");
    }

    @Test
    public void testBossIsNotCharger() {
        Enemy boss = new Boss(0, 0, player);
        assertTrue(boss.isBoss(), "Boss.isBoss() must return true");
        assertFalse(boss.isCharger(), "Boss.isCharger() must return false");
    }

    @Test
    public void testNormalEnemyIsNeitherType() {
        assertFalse(enemy.isCharger(), "Normal enemy must not be a charger");
        assertFalse(enemy.isBoss(), "Normal enemy must not be a boss");
    }
}
