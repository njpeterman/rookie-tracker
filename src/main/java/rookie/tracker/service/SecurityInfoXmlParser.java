package rookie.tracker.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SecurityInfoXmlParser {
	
	private Document doc;
	private DocumentBuilderFactory dbFactory;
	private File xmlFile;

	public SecurityInfoXmlParser(String fileName) {
		dbFactory = DocumentBuilderFactory.newInstance();
		this.xmlFile = new File(fileName);
	}
	
	/* Open XML File
	 * Call this function before calling any other 'Get' functions
	 */
	public void openXmlFile() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
	}
	
	public String getSpreadsheetId() {
		return doc.getElementsByTagName("spreadsheet_id").item(0).getTextContent();
	}
	
	public String getApiKey() {
		return doc.getElementsByTagName("api_key").item(0).getTextContent();
	}
	
	public String getClientSecretFilePath() {
		return doc.getElementsByTagName("client_secret_relative_file_path").item(0).getTextContent();
	}

}
