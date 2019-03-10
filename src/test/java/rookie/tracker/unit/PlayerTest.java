package rookie.tracker.unit;


import org.junit.Test;
import rookie.tracker.model.Player;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    private Player player;

    @Test
    public void testResolveRookiePlayerHasRookieStatus() {
        player = new Player(10, 40);
        assertTrue(player.isRookie());
    }

    @Test
    public void testResolveRookiePlayerHasTooManyAtBats() {
        player = new Player(131, 0);
        assertFalse(player.isRookie());
    }

    @Test
    public void testResolveRookiePlayerHasTooManyInningsPitched() {
        player = new Player(0, 50.1);
        assertFalse(player.isRookie());
    }

}
