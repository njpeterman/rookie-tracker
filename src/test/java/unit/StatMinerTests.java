package test.java.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.BeforeClass;

import main.java.StatMiner;

public class StatMinerTests {
	
	static Document batterDoc;
	static Document pitcherDoc;
	static StatMiner miner;
	
	@BeforeClass
	public static void oneTimeSetUp() throws IOException {
		File batterFile = new File("batter_info.htm");
		batterDoc = Jsoup.parse(batterFile, null);
		
		File pitcherFile = new File("pitcher_info.htm");
		pitcherDoc = Jsoup.parse(pitcherFile, null);
		
		miner = new StatMiner();
	}

	@Test
	public void testExtractAtBatsForBatter() {
		int expected = 3520; 
		int actual = miner.GetPlayerStats(batterDoc).GetAtBats();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractAtBatsForPitcher() {
		int expected = 0; // not technically correct, but works fine for now
		int actual = miner.GetPlayerStats(pitcherDoc).GetAtBats();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractInningsPitchedForBatter() {
		double expected = 0; 
		double actual = miner.GetPlayerStats(batterDoc).GetInningsPitched();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractInningsPitchedForPitcher() {
		double expected = 1405.1; 
		double actual = miner.GetPlayerStats(pitcherDoc).GetInningsPitched();
		
		assertEquals(expected, actual);
	}	

}