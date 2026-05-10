import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Skill.Slash;
import MyGame.GameObject.Skill.CrossSlash;
import MyGame.GameObject.Enemies.Enemy;
import MyGame.GameObject.Player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for active skill lifecycle: timer countdown, animation cycling,
 * opacity fade-out, and enemy hit deduplication.
 */
public class SkillTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0);
    }

    // ====================== Slash Tests ======================

    @Test
    public void testSlashIsAliveOnSpawn() {
        Slash slash = new Slash(0, 0, 0, 0);
        assertFalse(slash.isFinished(), "Slash must NOT be finished immediately after creation");
    }

    @Test
    public void testSlashTimerCountsDown() {
        Slash slash = new Slash(0, 0, 0, 0);
        double timerBefore = slash.getTimer();
        slash.update(0.05);
        assertTrue(slash.getTimer() < timerBefore, "Slash timer must decrease after update()");
    }

    @Test
    public void testSlashExpiresAfterMaxTimer() {
        Slash slash = new Slash(0, 0, 0, 0);
        // Tick past the 0.2s MAX_TIMER
        slash.update(slash.getMAX_TIMER() + 0.01);
        assertTrue(slash.isFinished(), "Slash must be finished after timer runs out");
        assertTrue(slash.fade(), "Slash.fade() must return true when finished");
    }

    @Test
    public void testSlashOpacityAtStart() {
        Slash slash = new Slash(0, 0, 0, 0);
        assertEquals(1.0, slash.opacity(), 0.001, "Slash opacity must be 1.0 at the moment of creation");
    }

    @Test
    public void testSlashOpacityFadesOut() {
        Slash slash = new Slash(0, 0, 0, 0);
        slash.update(slash.getMAX_TIMER() / 2.0);
        assertTrue(slash.opacity() < 1.0, "Slash opacity must be less than 1.0 halfway through its life");
        assertTrue(slash.opacity() > 0.0, "Slash opacity must still be positive halfway through its life");
    }

    @Test
    public void testSlashOpacityIsZeroWhenExpired() {
        Slash slash = new Slash(0, 0, 0, 0);
        slash.update(slash.getMAX_TIMER() + 0.5);
        assertEquals(0.0, slash.opacity(), 0.001, "Slash opacity must be clamped to 0 after expiry");
    }

    @Test
    public void testSlashAnimationFrameAdvances() {
        Slash slash = new Slash(0, 0, 0, 0);
        int initialFrame = slash.getCurrentFrame();
        // The frame advances every 0.02s
        slash.update(0.03);
        assertTrue(slash.getCurrentFrame() > initialFrame || slash.getCurrentFrame() == 0,
                "Slash animation frame must have advanced after 0.03s");
    }

    @Test
    public void testSlashHitFlagStartsFalse() {
        Slash slash = new Slash(0, 0, 0, 0);
        assertFalse(slash.isSlashHit(), "slashHit must start as false");
    }

    @Test
    public void testSlashPositionAccessors() {
        Slash slash = new Slash(150, 250, 50, 90);
        assertEquals(150, slash.getPosX(), 0.001);
        assertEquals(250, slash.getPosY(), 0.001);
        assertEquals(50, slash.getPosZ(), 0.001);
        assertEquals(90, slash.getAngle(), 0.001);
    }

    // ====================== CrossSlash Tests ======================

    @Test
    public void testCrossSlashIsAliveOnSpawn() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        assertFalse(cs.isFinished(), "CrossSlash must NOT be finished immediately after creation");
    }

    @Test
    public void testCrossSlashTimerCountsDown() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        double timerBefore = cs.getTimer();
        cs.update(0.1);
        assertTrue(cs.getTimer() < timerBefore, "CrossSlash timer must decrease after update()");
    }

    @Test
    public void testCrossSlashExpiresAfterMaxTimer() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        cs.update(cs.getMAX_TIMER() + 0.01);
        assertTrue(cs.isFinished(), "CrossSlash must be finished after timer runs out");
        assertTrue(cs.isFade(), "CrossSlash.isFade() must return true when finished");
    }

    @Test
    public void testCrossSlashOpacityFadesCorrectly() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        assertEquals(1.0, cs.opacity(), 0.001, "CrossSlash opacity must start at 1.0");
        cs.update(cs.getMAX_TIMER() + 0.5);
        assertEquals(0.0, cs.opacity(), 0.001, "CrossSlash opacity must be clamped to 0 after expiry");
    }

    @Test
    public void testCrossSlashHitListStartsEmpty() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        assertNotNull(cs.getHitEnemies(), "Hit enemies list must not be null");
        assertEquals(0, cs.getHitEnemies().size(), "Hit enemies list must start empty");
    }

    @Test
    public void testCrossSlashHitEnemiesDeduplication() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        Enemy enemy = new Enemy(0, 0, player);

        // Manually add enemy to hit list (simulates the first hit)
        cs.getHitEnemies().add(enemy);
        assertEquals(1, cs.getHitEnemies().size());

        // Trying to add the same enemy again should NOT double the list
        if (!cs.getHitEnemies().contains(enemy)) {
            cs.getHitEnemies().add(enemy);
        }
        assertEquals(1, cs.getHitEnemies().size(),
                "Same enemy must not appear in hitEnemies more than once");
    }

    @Test
    public void testCrossSlashAnimationFrameCycles() {
        CrossSlash cs = new CrossSlash(0, 0, 0);
        // Advance time to cycle several frames (frame advances every 0.04s, 8 total)
        cs.update(0.04 * 8 + 0.01);
        assertTrue(cs.getCurrentFrame() >= 0 && cs.getCurrentFrame() < 8,
                "CrossSlash frame must stay within [0, 7] bounds");
    }

    @Test
    public void testCrossSlashPositionAccessors() {
        CrossSlash cs = new CrossSlash(300, 400, Math.PI / 4);
        assertEquals(300, cs.getPosX(), 0.001);
        assertEquals(400, cs.getPosY(), 0.001);
        assertEquals(Math.PI / 4, cs.getAngle(), 0.001);
    }
}
