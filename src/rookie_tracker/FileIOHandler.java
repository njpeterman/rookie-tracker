package rookie_tracker;
import java.io.*;

public class FileIOHandler {
	private BufferedReader reader;
	private PrintWriter writer;
	
	/* Read Line
	 * Returns the next line in the input file opened from the 
	 * OpenInputFile method
	 * @returns a string, the line read
	 * Note: moves the file pointer in the input file. 
	 */
	public String ReadLine()
	{
		try {
			return reader.readLine();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}

	/* Read Line
	 * Appends the input string to the file opened in the OpenOutputFile function
	 * @param s, the string to append 
	 */
	public void WriteLine(String s) 
	{
		writer.println(s);
	}
	
	/* Close Input and Output Files
	 * This function should be explicitly called after concluding program use
	 */
	public void CloseFiles()
	{
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		writer.close();
	}
	
	public FileIOHandler(BufferedReader reader, PrintWriter writer)
	{
		this.reader = reader;
		this.writer = writer;
	}

	public FileIOHandler(String inputFile, String outputFile) throws FileNotFoundException
	{
		reader = new BufferedReader(new FileReader(inputFile));
		writer = new PrintWriter(outputFile);
	}
	
	protected void finalize() {
		this.CloseFiles();
	}
}
