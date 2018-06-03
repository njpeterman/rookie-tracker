package rookie_tracker;


public class Player {
	private int atBats; 
	private double inningsPitched;
	private int serviceTime; // in days
	private Boolean rookieStatus;
	
	public int GetAtBats() { return atBats; }
	public void SetAtBats(int newAtBats) { atBats = newAtBats; }
	public double GetInningsPitched() { return inningsPitched; }
	public void SetInningsPitched(double newIP) { inningsPitched = newIP; }
	public int GetServiceTime() { return serviceTime; }
	public void SetServiceTime(int newServiceTime) { serviceTime = newServiceTime; }
	public Boolean GetRookieStatus() { return rookieStatus; }
	public void SetRookieStatus(Boolean b) { rookieStatus = b; }
	
	public Player() 
	{
		atBats = serviceTime = 0;
		inningsPitched = 0;
	}
	
	public Player(int abs, double ip, int sTime)
	{
		atBats = abs;
		inningsPitched = ip;
		serviceTime = sTime;
	}
}
