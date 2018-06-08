package tests.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rookie_tracker.Player;
import rookie_tracker.RookieResolver;
import rookie_tracker.StatMiner;

import static org.mockito.Mockito.*;

class RookieResolverTests {

	static String html = "<html></html>";
	static RookieResolver rookieResolver;
	static Document document;
	static StatMiner miner; 
	Player player;

	@BeforeAll
	static void oneTimeSetUp() {
		document = Jsoup.parse(html);
		miner = mock(StatMiner.class);
		when(miner.GetDocument(anyString())).thenReturn(document);
		
		rookieResolver = new RookieResolver(miner);
	}
	
	@Test
	void testResolveRookiePlayerHasRookieStatus() {
		player = new Player(10, 40);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertTrue(actualPlayer.GetRookieStatus());
	}
	
	@Test
	void testResolveRookiePlayerHasTooManyAtBats() {
		player = new Player(131, 0);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertFalse(actualPlayer.GetRookieStatus());
	}
	
	@Test
	void testResolveRookiePlayerHasTooManyInningsPitched() {
		player = new Player(0, 50.1);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertFalse(actualPlayer.GetRookieStatus());
	}

}
