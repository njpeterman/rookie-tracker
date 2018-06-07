package tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import rookie_tracker.SecurityInfoXmlParser;

class SecurityInfoXmlParserTests {

	private static String xmlFilePath = "test_security_info.xml";
	private static SecurityInfoXmlParser parser; 

	@BeforeAll
	static void oneTimeSetUp() throws SAXException, IOException, ParserConfigurationException {
		parser = new SecurityInfoXmlParser(xmlFilePath);
		parser.OpenXmlFile();
	}
	
	@Test
	void testGetSpreadsheetId() {
		String expected = "random_id";
		String actual = parser.GetSpreadsheetId();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetApiKey() {
		String expected = "random_api_key";
		String actual = parser.GetApiKey();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetClientSecretFilePath() {
		String expected = "anywhere.json";
		String actual = parser.GetClientSecretFilePath();
		assertEquals(expected, actual);
	}

}
