package rookie_tracker;

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
			"C8:G8", // team 2
			"C14:G14", // ...
			"C20:G20",
			"C26:G26",
			"C32:G32",
			"C38:G38",
			"C44:G44",
			"C50:G50",
			"C56:G56",
			"C62:G62",
			"C68:G68", // team 12
		};
	
	private static String[] playerAtBatsRanges = {
			"C3:G3", // team 1
			"C9:G9", // team 2
			"C15:G15", // ...
			"C21:G21",
			"C27:G27",
			"C33:G33",
			"C39:G39",
			"C45:G45",
			"C51:G51",
			"C57:G57",
			"C63:G63",
			"C69:G69", // team 12
	};
	
	private static String[] playerInningsPitchedRanges = {
			"C4:G4", // team 1
			"C10:G10", // team 2
			"C16:G16", // ...
			"C22:G22",
			"C28:G28",
			"C34:G34",
			"C40:G40",
			"C46:G46",
			"C52:G52",
			"C58:G58",
			"C64:G64",
			"C70:G70", // team 12
	};
	
	private static String[] playerRookieStatusRanges = {
			"C5:G5", // team 1
			"C11:G11", // team 2
			"C17:G17", // ...
			"C23:G23",
			"C29:G29",
			"C35:G35",
			"C41:G41",
			"C47:G47",
			"C53:G53",
			"C59:G59",
			"C65:G65",
			"C71:G71", // team 12
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
