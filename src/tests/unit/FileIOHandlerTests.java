package tests.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rookie_tracker.*;

import static org.mockito.Mockito.*;

class FileIOHandlerTests {

	FileIOHandler handler;
	BufferedReader reader;
	PrintWriter writer;
	
	@BeforeEach
	void setUp() throws Exception {
		reader = mock(BufferedReader.class);
		writer = mock(PrintWriter.class);
		handler = new FileIOHandler(reader, writer);
	}

	@Test()
	void testReadLine() throws IOException
	{
		when(reader.readLine()).thenReturn("random");
		
		assertEquals("random", handler.ReadLine());
	}
	
	@Test()
	void testWriteLine() throws IOException
	{	
		handler.WriteLine("random");
		
		verify(writer, times(1)).println("random");		
	}
	
	@Test() 
	void testCloseAllFiles() throws IOException
	{
		handler.CloseFiles();
		
		verify(reader, times(1)).close();
		verify(writer, times(1)).close();
	}

}
