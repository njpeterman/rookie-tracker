package test.java.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;

import main.java.SecurityInfoXmlParser;

public class SecurityInfoXmlParserTests {

	private static String xmlFilePath = "test_security_info.xml";
	private static SecurityInfoXmlParser parser; 

	@BeforeClass
	public static void oneTimeSetUp() throws SAXException, IOException, ParserConfigurationException {
		parser = new SecurityInfoXmlParser(xmlFilePath);
		parser.OpenXmlFile();
	}
	
	@Test
	public void testGetSpreadsheetId() {
		String expected = "random_id";
		String actual = parser.GetSpreadsheetId();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetApiKey() {
		String expected = "random_api_key";
		String actual = parser.GetApiKey();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetClientSecretFilePath() {
		String expected = "anywhere.json";
		String actual = parser.GetClientSecretFilePath();
		assertEquals(expected, actual);
	}

}
