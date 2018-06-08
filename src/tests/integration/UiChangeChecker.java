package tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rookie_tracker.Player;
import rookie_tracker.RookieResolver;

class UiChangeChecker {

	static RookieResolver resolver;
	
	@BeforeAll
	static void oneTimeSetUp() throws Exception {
		resolver = new RookieResolver();
	}

	@Test
	void testAtBatsUIHasNotChanged() {
		Player player = resolver.ResolveRookie("https://www.baseball-reference.com/players/c/correca01.shtml");
		assertNotEquals(0, player.GetAtBats());
	}
	
	@Test
	void testInningsPitchedUIHasNotChanged() {
		Player player = resolver.ResolveRookie("https://www.baseball-reference.com/players/n/nolaaa01.shtml");
		assertNotEquals(0.0, player.GetInningsPitched());
	}

}
