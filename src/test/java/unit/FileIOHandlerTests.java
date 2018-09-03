package test.java.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;
import org.junit.Before;

import main.java.FileIOHandler;
import static org.mockito.Mockito.*;

public class FileIOHandlerTests {

	FileIOHandler handler;
	BufferedReader reader;
	PrintWriter writer;
	
	@Before
	public void setUp() throws Exception {
		reader = mock(BufferedReader.class);
		writer = mock(PrintWriter.class);
		handler = new FileIOHandler(reader, writer);
	}
	
	@Test()
	public void testReadLine() throws IOException
	{
		when(reader.readLine()).thenReturn("random");
		
		assertEquals("random", handler.ReadLine());
	}
	
	@Test()
	public void testWriteLine() throws IOException
	{	
		handler.WriteLine("random");
		
		verify(writer, times(1)).println("random");		
	}
	
	@Test() 
	public void testCloseAllFiles() throws IOException
	{
		handler.CloseFiles();
		
		verify(reader, times(1)).close();
		verify(writer, times(1)).close();
	}

}
