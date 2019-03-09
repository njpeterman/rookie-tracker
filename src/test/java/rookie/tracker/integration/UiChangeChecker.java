package rookie.tracker.integration;

import org.junit.Before;
import org.junit.Test;
import rookie.tracker.model.Player;
import rookie.tracker.service.RookieResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UiChangeChecker {

	private static RookieResolver resolver;
	
	@Before
	public void setUp() {
		resolver = new RookieResolver();
	}

	@Test
	public void testAtBatsUIHasNotChanged() {
		Player player = resolver.resolveRookie("https://www.baseball-reference.com/players/g/griffke02.shtml");
		assertEquals(9801, player.getAtBats());
	}
	
	@Test
	public void testInningsPitchedUIHasNotChanged() {
		Player player = resolver.resolveRookie("https://www.baseball-reference.com/players/r/ryanno01.shtml");
		assertEquals(5386.0, player.getInningsPitched());
	}

    @Test
    public void testGracefulFailureWithBadUrl() {
        assertNull(resolver.resolveRookie("afjdsflhdsjfhsdjkfhdsjkfhdsjkfhdsjkfh"));
    }

}
