package rookie.tracker.service;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import rookie.tracker.model.Player;
import rookie.tracker.util.PlayerDocumentUtils;

@AllArgsConstructor
public class RookieResolver {

	private PlayerDocumentUtils miner;
	private DocumentRetriever documentRetriever;

	public RookieResolver() {
		miner = new PlayerDocumentUtils();
		documentRetriever = new DocumentRetriever();
	}
	
	public Player resolveRookie(String url) {
        Document doc = documentRetriever.getDocument(url);

        return (doc == null) ? null : miner.getPlayerStats(doc);
	}

}
