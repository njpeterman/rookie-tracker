package rookie.tracker.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import rookie.tracker.service.SecurityInfoXmlParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityInfoXmlParserTests {

	private static SecurityInfoXmlParser parser;

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		String xmlFilePath = Thread.currentThread().getContextClassLoader().getResource("test_security_info.xml").getPath();
		parser = new SecurityInfoXmlParser(xmlFilePath);
		parser.openXmlFile();
	}
	
	@Test
	public void testGetSpreadsheetId() {
		String expected = "random_id";
		String actual = parser.getSpreadsheetId();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetApiKey() {
		String expected = "random_api_key";
		String actual = parser.getApiKey();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetClientSecretFilePath() {
		String expected = "anywhere.json";
		String actual = parser.getClientSecretFilePath();
		assertEquals(expected, actual);
	}

}
