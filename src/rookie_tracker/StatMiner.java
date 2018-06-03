package rookie_tracker;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.*;

public class StatMiner {
	
	@SuppressWarnings("deprecation")
	public Document GetDocument(String url)
	{
		try {
			return Jsoup.connect(url).validateTLSCertificates(false).get();
		} catch (HttpStatusException e) {
			System.out.printf("Error: 404 status for URL: %s\n", url);
			return null; // fix
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Player GetPlayerStats(Document doc) 
	{
		Player player = new Player();
	
		player.SetAtBats(ExtractAtBats(doc));
		player.SetInningsPitched(ExtractInningsPitched(doc));
		player.SetServiceTime(ExtractServiceTime(doc));
		
		return player;
	}
	
	private int ExtractAtBats(Document doc)
	{
		Element h4 = doc.select("h4.poptip[data-tip='At Bats']").first();
		
		if(h4 == null)
			return 0;
		
		return Integer.parseInt(h4.parent().children().last().text());
	}
	
	private double ExtractInningsPitched(Document doc)
	{
		Element h4 = doc.select("h4.poptip[data-tip='Innings Pitched']").first();
		
		if(h4 == null)
			return 0;
		
		return Double.parseDouble(h4.parent().children().last().text());
	}
	
	private int ExtractServiceTime(Document doc)
	{
		Element h4 = doc.select("p:contains(Service Time)").first();
		
		if(h4 == null)
			return 0;
		else {
			String sTime = h4.text().split("[:]+")[1].split("[ ]+")[1];
			return (int) (Double.parseDouble(sTime) * 1000);
		}
	}
	
}
