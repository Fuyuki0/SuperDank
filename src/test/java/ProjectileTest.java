import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Projectile.LightningEffect;
import java.util.ArrayList;

public class ProjectileTest {

    @Test
    public void testLightningFadingLogic() {
        // Create an empty strike list for the test
        LightningEffect lightning = new LightningEffect(new ArrayList<>());

        // Assert it is alive when spawned
        assertFalse(lightning.isDone(), "Lightning should not be done immediately");

        // Force the timer to simulate 1 second passing (past its MAX_TIMER)
        lightning.update(1.0);

        // Assert it tells the World it is ready to be deleted
        assertTrue(lightning.isDone(), "Lightning must report isDone() when timer runs out");
        assertTrue(lightning.opacity() <= 0, "Lightning opacity should be 0 when dead");
    }
}