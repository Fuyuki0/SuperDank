import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.DamageText;

/**
 * Tests for the DamageText visual indicator lifecycle:
 * timer, fade, text content, Y-axis float, opacity, and crit flag.
 */
public class DamageTextTest {

    @Test
    public void testDamageTextIsAliveOnSpawn() {
        DamageText dt = new DamageText(0, 0, 100.0, false);
        assertFalse(dt.isFade(), "DamageText must NOT be faded immediately after creation");
    }

    @Test
    public void testDamageTextTimerCountsDown() {
        DamageText dt = new DamageText(0, 0, 50.0, false);
        double before = dt.getTimer();
        dt.update(0.1);
        assertTrue(dt.getTimer() < before, "DamageText timer must decrease after update()");
    }

    @Test
    public void testDamageTextFadesAfterMaxTimer() {
        DamageText dt = new DamageText(0, 0, 50.0, false);
        dt.update(dt.getMAX_TIMER() + 0.01);
        assertTrue(dt.isFade(), "DamageText must be faded after timer runs out");
    }

    @Test
    public void testDamageTextOpacityStartsPositive() {
        DamageText dt = new DamageText(0, 0, 50.0, false);
        assertTrue(dt.getDamageTextOpacity() > 0, "Opacity must be positive at creation");
    }

    @Test
    public void testDamageTextOpacityIsZeroAfterFade() {
        DamageText dt = new DamageText(0, 0, 50.0, false);
        dt.update(dt.getMAX_TIMER() + 1.0);
        assertEquals(0.0, dt.getDamageTextOpacity(), 0.001, "Opacity must be 0.0 after the timer expires");
    }

    @Test
    public void testDamageTextFloatsUpward() {
        DamageText dt = new DamageText(0, 100, 50.0, false);
        // Note: position is randomized ± 20, so we test relative movement
        double yBefore = dt.getPosY();
        dt.update(0.1);
        assertTrue(dt.getPosY() < yBefore, "DamageText must float upward (Y decreases) over time");
    }

    @Test
    public void testDamageTextDisplaysCorrectNumber() {
        DamageText dt = new DamageText(0, 0, 123.7, false);
        // Text is formatted as int: (int)123.7 = 123
        assertEquals("123", dt.getText(), "DamageText must display the damage value as an integer string");
    }

    @Test
    public void testDamageTextDisplaysStringOverload() {
        DamageText dt = new DamageText(0, 0, "Dodge!", false);
        assertEquals("Dodge!", dt.getText(), "DamageText string constructor must preserve the exact string");
    }

    @Test
    public void testCritFlagSetCorrectly() {
        DamageText critDt = new DamageText(0, 0, 200.0, true);
        DamageText normalDt = new DamageText(0, 0, 200.0, false);
        assertTrue(critDt.isCrit(), "DamageText must report isCrit=true when constructed with true");
        assertFalse(normalDt.isCrit(), "DamageText must report isCrit=false when constructed with false");
    }

    @Test
    public void testSmoothMultiplierGrowsOverTime() {
        DamageText dt = new DamageText(0, 0, 50.0, false);
        double smoothBefore = dt.getSmoothMultiply();
        dt.update(0.1);
        assertTrue(dt.getSmoothMultiply() > smoothBefore,
                "Smooth multiplier must grow after each update");
    }

    @Test
    public void testDamageTextTextCanBeChanged() {
        DamageText dt = new DamageText(0, 0, 100.0, false);
        dt.setText("MISS");
        assertEquals("MISS", dt.getText(), "DamageText text must update via setText()");
    }
}
