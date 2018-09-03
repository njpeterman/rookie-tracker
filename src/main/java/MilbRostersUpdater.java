package main.java;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.api.services.sheets.v4.model.ValueRange;

public class MilbRostersUpdater {

	private static String[] playerLinkRanges = {
			"C2:G2", // team 1
			"C9:G9", // team 2
			"C16:G16", // ...
			"C23:G23",
			"C30:G30",
			"C37:G37",
			"C44:G44",
			"C51:G51",
			"C58:G58",
			"C65:G65",
			"C72:G72",
			"C79:G79", // team 12
		};
	
	private static String[] playerAtBatsRanges = {
			"C3:G3", // team 1
			"C10:G10", // team 2
			"C17:G17", // ...
			"C24:G24",
			"C31:G31",
			"C38:G38",
			"C45:G45",
			"C52:G52",
			"C59:G59",
			"C66:G66",
			"C73:G73",
			"C80:G80", // team 12
	};
	
	private static String[] playerInningsPitchedRanges = {
			"C4:G4", // team 1
			"C11:G11", // team 2
			"C18:G18", // ...
			"C25:G25",
			"C32:G32",
			"C39:G39",
			"C46:G46",
			"C53:G53",
			"C60:G60",
			"C67:G67",
			"C74:G74",
			"C81:G81", // team 12
	};
	
	private static String[] playerRookieStatusRanges = {
			"C6:G6", // team 1
			"C13:G13", // team 2
			"C20:G20", // ...
			"C27:G27",
			"C34:G34",
			"C41:G41",
			"C48:G48",
			"C55:G55",
			"C62:G62",
			"C69:G69",
			"C76:G76",
			"C83:G83", // team 12
	};
	
	public static void main(String[] args) throws IOException, GeneralSecurityException, SAXException, ParserConfigurationException {
		// Get info about spreadsheet ID, API key, and client secret
		SecurityInfoXmlParser xmlFileParser = new SecurityInfoXmlParser("milb_roster_spreadsheet_security_info.xml");
		xmlFileParser.OpenXmlFile();
		String spreadsheetId = xmlFileParser.GetSpreadsheetId();
		String apiKey = xmlFileParser.GetApiKey();
		String clientSecretFilePath = xmlFileParser.GetClientSecretFilePath();
		
		// Get the Baseball Reference Player Links for each team
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
		List<ValueRange> response = 
				wrapper.GetBatchValues(spreadsheetId, apiKey, clientSecretFilePath, new ArrayList<>(Arrays.asList(playerLinkRanges))).getValueRanges();
		
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
				Player playerStats = resolver.ResolveRookie(playerLink.toString());
				if(playerStats != null) {
					atBats.add(Integer.toString(playerStats.GetAtBats()));
					inningsPitched.add(Double.toString(playerStats.GetInningsPitched()));
					rookieStatus.add(Boolean.toString(playerStats.GetRookieStatus()).toUpperCase());
				} else {
					atBats.add("N/A");
					inningsPitched.add("N/A");
					rookieStatus.add("NOT FOUND");
				}
				
			}
			
			atBatsVR.setValues(new ArrayList<List<Object>>(Arrays.asList(atBats)));
			inningsPitchedVR.setValues(new ArrayList<List<Object>>(Arrays.asList(inningsPitched)));
			rookieStatusVR.setValues(new ArrayList<List<Object>>(Arrays.asList(rookieStatus)));
			
			atBatsVR.setRange(playerAtBatsRanges[i]);
			inningsPitchedVR.setRange(playerInningsPitchedRanges[i]);
			rookieStatusVR.setRange(playerRookieStatusRanges[i]);
			
			playerAtBatsData.add(atBatsVR);
			playerInningsPitchedData.add(inningsPitchedVR);
			playerRookieStatusData.add(rookieStatusVR);
		}
	
		wrapper.UpdateBatchValues(spreadsheetId, apiKey, clientSecretFilePath, playerAtBatsData);
		wrapper.UpdateBatchValues(spreadsheetId, apiKey, clientSecretFilePath, playerInningsPitchedData);
		wrapper.UpdateBatchValues(spreadsheetId, apiKey, clientSecretFilePath, playerRookieStatusData);		
	}

}
