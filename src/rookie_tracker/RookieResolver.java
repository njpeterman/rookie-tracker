package rookie_tracker;

import org.jsoup.nodes.Document;

public class RookieResolver { 
	
	StatMiner miner; 
	
	public Player ResolveRookie(String url)
	{
		Player player = GetPlayerStats(url);
		
		if(player == null)
		{
			System.out.printf("Invalid url: %s\n", url);
			System.exit(1);
		}
		
		player.SetRookieStatus(IsRookie(player));
		// System.out.printf("ABs: %d, IP: %.1f, Service Time: %d\n", player.GetAtBats(), player.GetInningsPitched(), player.GetServiceTime());
		
		return player;
	}
	
	private Player GetPlayerStats(String url) 
	{
		Document doc = miner.GetDocument(url);
		
		return (doc != null) ? miner.GetPlayerStats(doc) : null;
	}
	
	private Boolean IsRookie(Player player)
	{
		return (player.GetAtBats() <= 130) && (player.GetInningsPitched() <= 50) 
				&& (player.GetServiceTime() <= 45);
	}
	
	public RookieResolver(StatMiner m) {
		miner = m;
	}
	
	public RookieResolver() {
		miner = new StatMiner();
	}
}
