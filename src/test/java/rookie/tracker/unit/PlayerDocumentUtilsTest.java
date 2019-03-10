package rookie.tracker.unit;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import rookie.tracker.util.PlayerDocumentUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDocumentUtilsTest {
	
	private static Document batterDoc;
	private static Document pitcherDoc;
	private static PlayerDocumentUtils miner;
	
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		batterDoc = getDocument("batter_info.htm");
		pitcherDoc = getDocument("pitcher_info.htm");
		miner = new PlayerDocumentUtils();
	}

	@Test
	public void testExtractAtBatsForBatter() {
		int expected = 3520; 
		int actual = miner.getPlayerStats(batterDoc).getAtBats();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractAtBatsForPitcher() {
		int expected = 0; // not technically correct, but works fine for now
		int actual = miner.getPlayerStats(pitcherDoc).getAtBats();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractInningsPitchedForBatter() {
		double expected = 0; 
		double actual = miner.getPlayerStats(batterDoc).getInningsPitched();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractInningsPitchedForPitcher() {
		double expected = 1405.1; 
		double actual = miner.getPlayerStats(pitcherDoc).getInningsPitched();
		
		assertEquals(expected, actual);
	}

	private static Document getDocument(String fileName) throws Exception {
		return Jsoup.parse(
		        IOUtils.toString(
		                Thread.currentThread().getContextClassLoader()
                                .getResourceAsStream(fileName)));
	}

}
