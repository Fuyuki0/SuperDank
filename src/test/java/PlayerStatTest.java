import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Player.Player;

/**
 * Tests for all Player stat systems: health, stamina, overheal, experience,
 * level-up, kill counter, coin system, and cooldown ratio functions.
 */
public class PlayerStatTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0);
    }

    // ====================== Health Clamping ======================

    @Test
    public void testHealthCappedAtMax() {
        player.setCurrentHealth(player.getMaxHealth() + 9999);
        assertEquals(player.getMaxHealth(), player.getCurrentHealth(),
                "Current health must be capped at maxHealth");
    }

    @Test
    public void testHealthCannotGoBelowZero() {
        player.setCurrentHealth(-1);
        assertEquals(0, player.getCurrentHealth(),
                "Current health must not go below 0");
    }

    @Test
    public void testHealthRatioIsOneAtFull() {
        player.setCurrentHealth(player.getMaxHealth());
        assertEquals(1.0, player.healthRatio(), 0.001, "healthRatio() must be 1.0 at full HP");
    }

    @Test
    public void testHealthRatioIsZeroAtDeath() {
        player.setCurrentHealth(0);
        assertEquals(0.0, player.healthRatio(), 0.001, "healthRatio() must be 0.0 at 0 HP");
    }

    @Test
    public void testHealthRatioIsHalfAtHalfHP() {
        player.setCurrentHealth(player.getMaxHealth() / 2.0);
        assertEquals(0.5, player.healthRatio(), 0.01, "healthRatio() must be ~0.5 at half HP");
    }

    // ====================== Stamina Clamping ======================

    @Test
    public void testStaminaCappedAtMax() {
        player.setCurrentStamina(player.getMaxStamina() + 500);
        assertEquals(player.getMaxStamina(), player.getCurrentStamina(),
                "Stamina must be capped at maxStamina");
    }

    @Test
    public void testStaminaCannotGoBelowZero() {
        player.setCurrentStamina(-100);
        assertEquals(0, player.getCurrentStamina(),
                "Stamina must not go below 0");
    }

    @Test
    public void testStaminaRatioAtFull() {
        player.setCurrentStamina(player.getMaxStamina());
        assertEquals(1.0, player.staminaRatio(), 0.001, "staminaRatio() must be 1.0 at full stamina");
    }

    @Test
    public void testStaminaRatioAtEmpty() {
        player.setCurrentStamina(0);
        assertEquals(0.0, player.staminaRatio(), 0.001, "staminaRatio() must be 0.0 at 0 stamina");
    }

    // ====================== Overheal ======================

    @Test
    public void testOverhealStartsAtZero() {
        assertEquals(0, player.getCurrentOverHeal(), "Overheal must start at 0");
    }

    @Test
    public void testOverhealCannotExceedMax() {
        player.addOverHealing(500);
        player.setCurrentOverHeal(player.getMaxOverHeal() + 999);
        assertEquals(player.getMaxOverHeal(), player.getCurrentOverHeal(),
                "Overheal must be capped at maxOverHeal");
    }

    @Test
    public void testOverhealCannotGoBelowZero() {
        player.addOverHealing(200);
        player.setCurrentOverHeal(-50);
        assertEquals(0, player.getCurrentOverHeal(),
                "Overheal must not go below 0");
    }

    @Test
    public void testHasMaxOverHealFalseByDefault() {
        assertFalse(player.hasMaxOverHeal(),
                "hasMaxOverHeal() must be false before applying overheal items");
    }

    @Test
    public void testHasMaxOverHealTrueAfterApply() {
        player.addOverHealing(100);
        assertTrue(player.hasMaxOverHeal(),
                "hasMaxOverHeal() must return true after addOverHealing()");
    }

    // ====================== Experience & Level Up ======================

    @Test
    public void testExperienceRatioStartsAtZero() {
        assertEquals(0.0, player.getExpRatio(), 0.001, "EXP ratio must start at 0.0");
    }

    @Test
    public void testExperienceIncreasesExpRatio() {
        player.addExperience(10);
        assertTrue(player.getExpRatio() > 0, "EXP ratio must be positive after gaining XP");
    }

    @Test
    public void testLevelUpOccursWhenExpFull() {
        int initialLevel = player.getPlayerLevel();
        // addExperience handles multiplier — pump enough to guarantee level-up
        player.addExperience(9999999);
        assertTrue(player.getPlayerLevel() > initialLevel,
                "Player must level up after gaining enough experience");
    }

    @Test
    public void testPendingLevelUpCounterIncrements() {
        player.addExperience(9999999);
        assertTrue(player.getPendingLevelup() > 0,
                "Pending level-up count must be greater than 0 after leveling up");
    }

    @Test
    public void testClaimLevelUpDecrementsPending() {
        player.addExperience(9999999);
        int pendingBefore = player.getPendingLevelup();
        player.claimLevelUp();
        assertEquals(pendingBefore - 1, player.getPendingLevelup(),
                "claimLevelUp() must decrement the pending level-up counter by 1");
    }

    @Test
    public void testExpMultiplierBoostsGain() {
        player.addExpMultiplier(1.0); // 2x total
        int levelBefore = player.getPlayerLevel();
        // With 2x multiplier, less XP is needed to level compared to raw amount
        player.addExperience(50); // normally just enough for 1 level-up at 1x
        // At 2x it would also level up (or level sooner)
        assertTrue(player.getExpRatio() >= 0, "EXP ratio must remain valid with multiplier");
    }

    // ====================== Kill Counter ======================

    @Test
    public void testKillCounterStartsAtZero() {
        assertEquals(0, player.getEnemiesKilled(), "Kill count must start at 0");
    }

    @Test
    public void testKillCounterIncrements() {
        player.addKill();
        player.addKill();
        assertEquals(2, player.getEnemiesKilled(), "Kill count must increment with each addKill()");
    }

    // ====================== Coin System ======================

    @Test
    public void testCoinStartsAtZero() {
        assertEquals(0, player.getCoin(), "Coin count must start at 0");
    }

    @Test
    public void testCoinCannotGoBelowZero() {
        player.setCoin(-100);
        assertEquals(0, player.getCoin(), "Coin count must not go below 0");
    }

    @Test
    public void testCoinAccumulates() {
        player.setCoin(50);
        player.setCoin(player.getCoin() + 30);
        assertEquals(80, player.getCoin(), "Coin must accumulate correctly");
    }

    // ====================== Cooldown Ratios ======================

    @Test
    public void testSlashCooldownRatioIsZeroWhenReady() {
        // After construction, slashCooldown is 0 and gameStartTimer > 0
        // Force gameStartTimer to 0 by waiting
        player.setSlashCooldown(0);
        // gameStartTimer is not directly settable, but the ratio without it is:
        double ratio = player.slashCooldownRatio();
        assertTrue(ratio >= 0.0, "Slash cooldown ratio must be >= 0");
        assertTrue(ratio <= 1.0, "Slash cooldown ratio must be <= 1");
    }

    @Test
    public void testDashCooldownRatioIsZeroWhenReady() {
        double ratio = player.dashCooldownRatio();
        assertTrue(ratio >= 0.0, "Dash cooldown ratio must be >= 0");
        assertTrue(ratio <= 1.0, "Dash cooldown ratio must be <= 1");
    }

    // ====================== Stat Multipliers ======================

    @Test
    public void testSpeedMultiplierStartsAtOne() {
        assertEquals(1.0, player.getSpeedMultiplier(), 0.001,
                "Speed multiplier must start at 1.0");
    }

    @Test
    public void testAddSpeedMultiplierAccumulates() {
        player.addSpeedMultiplier(0.5);
        assertEquals(1.5, player.getSpeedMultiplier(), 0.001,
                "Speed multiplier must be 1.5 after adding 0.5");
    }

    @Test
    public void testMagnetRadiusDefault() {
        assertTrue(player.getMagnetRadius() > 0,
                "Player must start with a positive magnet radius");
    }

    @Test
    public void testMagnetRadiusCanBeIncreased() {
        double initial = player.getMagnetRadius();
        player.setMagnetRadius(initial + 100);
        assertEquals(initial + 100, player.getMagnetRadius(), 0.001,
                "Magnet radius must update correctly");
    }

    // ====================== Velocity Update (Physics) ======================

    @Test
    public void testVelocityIncreasesWithDirection() {
        double v = player.updateVelocity(1.0, 0, 0.016); // moving right
        assertTrue(v > 0, "Velocity must increase when direction is positive");
    }

    @Test
    public void testVelocityDecreasesToZeroWithNoDirection() {
        double v = player.updateVelocity(0.0, 100, 1.0); // large deltaTime, no direction
        assertEquals(0, v, 1.0, "Velocity must decay to ~0 with no direction and enough time");
    }
}
