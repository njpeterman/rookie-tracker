package rookie.tracker;

import rookie.tracker.service.GoogleSheetsApiWrapper;
import rookie.tracker.service.MilbRostersUpdater;
import rookie.tracker.service.RookieResolver;
import rookie.tracker.service.SecurityInfoXmlParser;

public class Main {
	
	public static void main(String[] args) throws Exception {
		// Get info about spreadsheet ID, API key, and client secret
		SecurityInfoXmlParser xmlFileParser = new SecurityInfoXmlParser("milb_roster_spreadsheet_security_info.xml");
		xmlFileParser.openXmlFile();
		String spreadsheetId = xmlFileParser.getSpreadsheetId();
		String apiKey = xmlFileParser.getApiKey();
		String clientSecretFilePath = xmlFileParser.getClientSecretFilePath();

        MilbRostersUpdater rostersUpdater = new MilbRostersUpdater(new GoogleSheetsApiWrapper(), new RookieResolver());
        rostersUpdater.updateRostersSpreadsheet(spreadsheetId, apiKey, clientSecretFilePath);

	}

}
