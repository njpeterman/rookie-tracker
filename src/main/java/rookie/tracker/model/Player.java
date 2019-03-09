package rookie.tracker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
	private int atBats;
	private double inningsPitched;
	
	public Player(int abs, double ip) {
		atBats = abs;
		inningsPitched = ip;
	}

    public boolean isRookie() {
        return (atBats <= 130) && (inningsPitched <= 50);
    }
}
