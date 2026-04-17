import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.GameObject.Weapon.Sword;
import MyGame.GameObject.Weapon.Weapon;
import MyGame.GameObject.Weapon.WeaponType;

public class WeaponTest {

    private Weapon testWeapon;

    @BeforeEach
    public void setUp() {
        testWeapon = new Sword();
    }

    @Test
    public void testWeaponInitialization() {
        assertEquals(WeaponType.SWORD, testWeapon.getType(), "Sword must have SWORD type");
        assertEquals(1, testWeapon.getLevel(), "Weapons must start at level 1");
    }

    @Test
    public void testWeaponStatAccumulation() {
        double initialDamage = testWeapon.getBonusDamage();

        // Simulate picking up a duplicate weapon to upgrade stats
        testWeapon.addBonusDamage(15.0);
        testWeapon.addBonusAttackSpeed(10.0);

        assertEquals(initialDamage + 15.0, testWeapon.getBonusDamage(), "Bonus damage must accumulate");
        assertEquals(10.0, testWeapon.getBonusAttackSpeed(), "Attack speed must accumulate");
    }
}