package test.java.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.BeforeClass;

import main.java.Player;
import main.java.RookieResolver;
import main.java.StatMiner;

import static org.mockito.Mockito.*;

public class RookieResolverTests {

	static String html = "<html></html>";
	static RookieResolver rookieResolver;
	static Document document;
	static StatMiner miner; 
	Player player;

	@BeforeClass
	public static void oneTimeSetUp() {
		document = Jsoup.parse(html);
		miner = mock(StatMiner.class);
		when(miner.GetDocument(anyString())).thenReturn(document);
		
		rookieResolver = new RookieResolver(miner);
	}
	
	@Test
	public void testResolveRookiePlayerHasRookieStatus() {
		player = new Player(10, 40);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertTrue(actualPlayer.GetRookieStatus());
	}
	
	@Test
	public void testResolveRookiePlayerHasTooManyAtBats() {
		player = new Player(131, 0);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertFalse(actualPlayer.GetRookieStatus());
	}
	
	@Test
	public void testResolveRookiePlayerHasTooManyInningsPitched() {
		player = new Player(0, 50.1);
		when(miner.GetPlayerStats(document)).thenReturn(player);
		
		Player actualPlayer = rookieResolver.ResolveRookie("fakewebsite.com");
		
		assertFalse(actualPlayer.GetRookieStatus());
	}

}
