package rookie.tracker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.api.services.sheets.v4.model.ValueRange;
import rookie.tracker.model.Player;
import rookie.tracker.service.GoogleSheetsAPIWrapper;
import rookie.tracker.service.RookieResolver;
import rookie.tracker.service.SecurityInfoXmlParser;

public class Main {

	private static String[] playerLinkRanges = {
			"C2:J2", // team 1
			"C9:J9", // team 2
			"C16:J16", // ...
			"C23:J23",
			"C30:J30",
			"C37:J37",
			"C44:J44",
			"C51:J51",
			"C58:J58",
			"C65:J65",
			"C72:J72",
			"C79:J79", // team 12
		};
	
	private static String[] playerAtBatsRanges = {
			"C3:J3", // team 1
			"C10:J10", // team 2
			"C17:J17", // ...
			"C24:J24",
			"C31:J31",
			"C38:J38",
			"C45:J45",
			"C52:J52",
			"C59:J59",
			"C66:J66",
			"C73:J73",
			"C80:J80", // team 12
	};
	
	private static String[] playerInningsPitchedRanges = {
			"C4:J4", // team 1
			"C11:J11", // team 2
			"C18:J18", // ...
			"C25:J25",
			"C32:J32",
			"C39:J39",
			"C46:J46",
			"C53:J53",
			"C60:J60",
			"C67:J67",
			"C74:J74",
			"C81:J81", // team 12
	};
	
	private static String[] playerRookieStatusRanges = {
			"C6:J6", // team 1
			"C13:J13", // team 2
			"C20:J20", // ...
			"C27:J27",
			"C34:J34",
			"C41:J41",
			"C48:J48",
			"C55:J55",
			"C62:J62",
			"C69:J69",
			"C76:J76",
			"C83:J83", // team 12
	};
	
	public static void main(String[] args) throws IOException, GeneralSecurityException, SAXException, ParserConfigurationException {
		// Get info about spreadsheet ID, API key, and client secret
		SecurityInfoXmlParser xmlFileParser = new SecurityInfoXmlParser("milb_roster_spreadsheet_security_info.xml");
		xmlFileParser.openXmlFile();
		String spreadsheetId = xmlFileParser.getSpreadsheetId();
		String apiKey = xmlFileParser.getApiKey();
		String clientSecretFilePath = xmlFileParser.getClientSecretFilePath();
		
		// Get the Baseball Reference Player Links for each team
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
		List<ValueRange> response = 
				wrapper.getBatchValues(spreadsheetId, apiKey, clientSecretFilePath, new ArrayList<>(Arrays.asList(playerLinkRanges))).getValueRanges();
		
		RookieResolver resolver = new RookieResolver();

		// One ValueRange per team. Each ValueRange will contain the data for the team's players.
		List<ValueRange> playerAtBatsData = new ArrayList<>();
		List<ValueRange> playerInningsPitchedData = new ArrayList<>();
		List<ValueRange> playerRookieStatusData = new ArrayList<>();
		
		for (int i = 0; i < response.size(); i++) { // For each team's list of player links
			ValueRange valueRange = response.get(i);
			
			List<Object> playerLinks = valueRange.getValues().get(0); // Data is in 1D row, so this is fine
			
			ValueRange atBatsVR = new ValueRange();
			ValueRange inningsPitchedVR = new ValueRange();
			ValueRange rookieStatusVR = new ValueRange();
			
			List<Object> atBats = new ArrayList<>();
			List<Object> inningsPitched = new ArrayList<>();
			List<Object> rookieStatus = new ArrayList<>();
			
			for (Object playerLink : playerLinks) {
				Player playerStats = resolver.resolveRookie(playerLink.toString());
				if(playerStats != null) {
					atBats.add(Integer.toString(playerStats.getAtBats()));
					inningsPitched.add(Double.toString(playerStats.getInningsPitched()));
					rookieStatus.add(Boolean.toString(playerStats.isRookie()).toUpperCase());
				} else {
					atBats.add("N/A");
					inningsPitched.add("N/A");
					rookieStatus.add("NOT FOUND");
				}
				
			}
			
			atBatsVR.setValues(new ArrayList<>(Arrays.asList(atBats)));
			inningsPitchedVR.setValues(new ArrayList<>(Arrays.asList(inningsPitched)));
			rookieStatusVR.setValues(new ArrayList<>(Arrays.asList(rookieStatus)));
			
			atBatsVR.setRange(playerAtBatsRanges[i]);
			inningsPitchedVR.setRange(playerInningsPitchedRanges[i]);
			rookieStatusVR.setRange(playerRookieStatusRanges[i]);
			
			playerAtBatsData.add(atBatsVR);
			playerInningsPitchedData.add(inningsPitchedVR);
			playerRookieStatusData.add(rookieStatusVR);
		}
	
		wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerAtBatsData);
		wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerInningsPitchedData);
		wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerRookieStatusData);
	}

}
