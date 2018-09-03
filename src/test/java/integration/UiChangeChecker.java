package test.java.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.Player;
import main.java.RookieResolver;

public class UiChangeChecker {

	static RookieResolver resolver;
	
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		resolver = new RookieResolver();
	}

	@Test
	public void testAtBatsUIHasNotChanged() {
		Player player = resolver.ResolveRookie("https://www.baseball-reference.com/players/c/correca01.shtml");
		assertNotEquals(0, player.GetAtBats());
	}
	
	@Test
	public void testInningsPitchedUIHasNotChanged() {
		Player player = resolver.ResolveRookie("https://www.baseball-reference.com/players/n/nolaaa01.shtml");
		assertNotEquals(0.0, player.GetInningsPitched());
	}

}
