package rookie_tracker;

import org.jsoup.nodes.Document;

public class RookieResolver { 
	
	StatMiner miner; 
	
	public Player ResolveRookie(String url)
	{
		Player player = GetPlayerStats(url);
		
		if(player == null)
			return null;
		
		player.SetRookieStatus(IsRookie(player));
		
		return player;
	}
	
	private Player GetPlayerStats(String url) 
	{
		Document doc = miner.GetDocument(url);
		
		return (doc != null) ? miner.GetPlayerStats(doc) : null;
	}
	
	private Boolean IsRookie(Player player)
	{
		return (player.GetAtBats() <= 130) && (player.GetInningsPitched() <= 50);
	}
	
	public RookieResolver(StatMiner m) {
		miner = m;
	}
	
	public RookieResolver() {
		miner = new StatMiner();
	}
}
