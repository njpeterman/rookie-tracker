package rookie.tracker.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.AllArgsConstructor;
import rookie.tracker.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static rookie.tracker.config.Constants.playerAtBatsRanges;
import static rookie.tracker.config.Constants.playerInningsPitchedRanges;
import static rookie.tracker.config.Constants.playerLinkRanges;
import static rookie.tracker.config.Constants.playerRookieStatusRanges;

@AllArgsConstructor
public class MilbRostersUpdater {

    private final GoogleSheetsApiWrapper wrapper;
    private final RookieResolver resolver;

    public void updateRostersSpreadsheet(String spreadsheetId, String apiKey, String clientSecretFilePath) throws Exception {
        // Get the Baseball Reference Player Links for each team
        List<ValueRange> response =
                wrapper.getBatchValues(spreadsheetId, apiKey, clientSecretFilePath, new ArrayList<>(Arrays.asList(playerLinkRanges))).getValueRanges();

        // One ValueRange per team. Each ValueRange will contain the data for the team's players.
        List<ValueRange> playerAtBatsData = new ArrayList<>();
        List<ValueRange> playerInningsPitchedData = new ArrayList<>();
        List<ValueRange> playerRookieStatusData = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) { // For each team's list of player links
            ValueRange valueRange = response.get(i);

            List<Object> playerLinks = valueRange.getValues().get(0); // Data is in 1D row, so this is fine

            ValueRange atBatsValueRange = new ValueRange();
            ValueRange inningsPitchedValueRange = new ValueRange();
            ValueRange rookieStatusValueRange = new ValueRange();

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

            atBatsValueRange.setValues(new ArrayList<>(Collections.singletonList(atBats)));
            inningsPitchedValueRange.setValues(new ArrayList<>(Collections.singletonList(inningsPitched)));
            rookieStatusValueRange.setValues(new ArrayList<>(Collections.singletonList(rookieStatus)));

            atBatsValueRange.setRange(playerAtBatsRanges[i]);
            inningsPitchedValueRange.setRange(playerInningsPitchedRanges[i]);
            rookieStatusValueRange.setRange(playerRookieStatusRanges[i]);

            playerAtBatsData.add(atBatsValueRange);
            playerInningsPitchedData.add(inningsPitchedValueRange);
            playerRookieStatusData.add(rookieStatusValueRange);
        }

        wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerAtBatsData);
        wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerInningsPitchedData);
        wrapper.updateBatchValues(spreadsheetId, clientSecretFilePath, playerRookieStatusData);
    }

}
