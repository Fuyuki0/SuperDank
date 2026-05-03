import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import MyGame.Interface.Item;
import MyGame.GameObject.Items.ItemManager;
import MyGame.GameObject.Player.Player;
import MyGame.Game.World;

public class ItemManagerTest {

    private Player player;
    private World world;

    @BeforeEach
    public void setUp() {
        player = new Player(0,0);
        world = new World();
        world.setPlayer(player);
    }

    @Test
    public void testChestRollReturnsValidItem() {
        // Make sure the player has 0 items to start
        assertEquals(0, world.getItemList().size());

        // Call the factory directly
        Item rolledItem = ItemManager.openingChest(world);

        // Prove the factory actually generated something
        assertNotNull(rolledItem, "Factory must never return a null item!");
        assertNotNull(rolledItem.getName(), "Rolled item must have a valid string name");
    }
}