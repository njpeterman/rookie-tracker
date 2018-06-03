package rookie_tracker;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
		String inputFile = new File("").getAbsolutePath().concat("/input.txt");
		String outputFile = new File("").getAbsolutePath().concat("/output.txt");
		
		FileIOHandler fileHandler = null;
		try {
			fileHandler = new FileIOHandler(inputFile, outputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Invalid input or output file");
			System.exit(1);
		}
		
		RookieResolver resolver = new RookieResolver();
		
		String input = null;
		while((input = fileHandler.ReadLine()) != null)
		{
			if(input.contains("www.baseball-reference.com"))
			{
				String playerLink = input;
				Player playerStats = resolver.ResolveRookie(playerLink);
				
				String output = String.format("%d ABs, %.1f IP, %d Days Service Time ... Is Rookie? %b", 
						playerStats.GetAtBats(), playerStats.GetInningsPitched(), 
						playerStats.GetServiceTime(), playerStats.GetRookieStatus());
				
				fileHandler.WriteLine(output);
			} else {
				fileHandler.WriteLine(input);
			}
		}
		
		fileHandler.CloseFiles();
	}

}
