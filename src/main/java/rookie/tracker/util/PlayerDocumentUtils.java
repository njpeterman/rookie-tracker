package rookie.tracker.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rookie.tracker.model.Player;

public class PlayerDocumentUtils {
	
	public Player getPlayerStats(Document doc) {
		return Player.builder()
                .atBats(extractAtBats(doc))
                .inningsPitched(extractInningsPitched(doc))
                .build();
	}
	
	private int extractAtBats(Document doc) {
		Element h4 = doc.select("h4.poptip[data-tip='At Bats']").first();
		
		if(h4 == null)
			return 0;
		
		return Integer.parseInt(h4.parent().children().last().text());
	}
	
	private double extractInningsPitched(Document doc) {
		Element h4 = doc.select("h4.poptip[data-tip='Innings Pitched']").first();
		
		if(h4 == null)
			return 0;
		
		return Double.parseDouble(h4.parent().children().last().text());
	}
	
}
