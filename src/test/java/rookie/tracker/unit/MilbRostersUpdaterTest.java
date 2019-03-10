package rookie.tracker.unit;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rookie.tracker.model.Player;
import rookie.tracker.service.GoogleSheetsApiWrapper;
import rookie.tracker.service.MilbRostersUpdater;
import rookie.tracker.service.RookieResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MilbRostersUpdaterTest {

    private BatchGetValuesResponse getValuesResponse;
    private ValueRange team1Range;
    private ValueRange team2Range;
    private List<List<Object>> team1PlayerLinks = new ArrayList<>();
    private List<List<Object>> team2PlayerLinks = new ArrayList<>();

    @Mock
    private GoogleSheetsApiWrapper googleSheetsAPIWrapper;

    @Mock
    private RookieResolver rookieResolver;

    @InjectMocks
    private MilbRostersUpdater milbRostersUpdater;

    private final String spreadsheetId = "spreadsheetId";
    private final String apiKey = "apiKey";
    private final String clientSecretFilePath = "clientSecretFilePath";

    private final String player1Link = "player1.com";
    private final String player2Link = "player2.net";
    private final String player3Link = "player3.gov";
    private final String player4Link = "player4.org";
    private final Player player1 = new Player(10, 15.1); // TRUE
    private final Player player2 = new Player(15, 65.2); // FALSE
    private final Player player3 = new Player(69, 2); // TRUE
    private final Player player4 = new Player(3000, 29); // FALSE

    @Before
    public void setUp() {
        team1PlayerLinks.add(new ArrayList<>(Arrays.asList(player1Link, player2Link)));
        team1Range = new ValueRange();
        team1Range.setValues(team1PlayerLinks);

        team2PlayerLinks.add(new ArrayList<>(Arrays.asList(player3Link, player4Link)));
        team2Range = new ValueRange();
        team2Range.setValues(team2PlayerLinks);

        getValuesResponse = new BatchGetValuesResponse();
        getValuesResponse.setValueRanges(new ArrayList<>(Arrays.asList(team1Range, team2Range)));

        when(rookieResolver.resolveRookie(player1Link)).thenReturn(player1);
        when(rookieResolver.resolveRookie(player2Link)).thenReturn(player2);
        when(rookieResolver.resolveRookie(player3Link)).thenReturn(player3);
        when(rookieResolver.resolveRookie(player4Link)).thenReturn(player4);
    }

    @Test
    public void updateRostersSpreadsheet_happyPath() throws Exception {
        when(googleSheetsAPIWrapper.getBatchValues(any(), any(), any(), any())).thenReturn(getValuesResponse);

        milbRostersUpdater.updateRostersSpreadsheet(spreadsheetId, apiKey, clientSecretFilePath);

        ValueRange team1AtBats = new ValueRange();
        team1AtBats.setRange("C3:J3");
        team1AtBats.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("10", "15"))));
        ValueRange team2AtBats = new ValueRange();
        team2AtBats.setRange("C10:J10");
        team2AtBats.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("69", "3000"))));
        verify(googleSheetsAPIWrapper).updateBatchValues(spreadsheetId, clientSecretFilePath, Arrays.asList(team1AtBats, team2AtBats));

        ValueRange team1InningsPitched = new ValueRange();
        team1InningsPitched.setRange("C4:J4");
        team1InningsPitched.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("15.1", "65.2"))));
        ValueRange team2InningsPitched = new ValueRange();
        team2InningsPitched.setRange("C11:J11");
        team2InningsPitched.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("2.0", "29.0"))));
        verify(googleSheetsAPIWrapper).updateBatchValues(spreadsheetId, clientSecretFilePath, Arrays.asList(team1InningsPitched, team2InningsPitched));

        ValueRange team1RookieStatuses = new ValueRange();
        team1RookieStatuses.setRange("C6:J6");
        team1RookieStatuses.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("TRUE", "FALSE"))));
        ValueRange team2RookieStatuses = new ValueRange();
        team2RookieStatuses.setRange("C13:J13");
        team2RookieStatuses.setValues(new ArrayList<>(Collections.singletonList(Arrays.asList("TRUE", "FALSE"))));
        verify(googleSheetsAPIWrapper).updateBatchValues(spreadsheetId, clientSecretFilePath, Arrays.asList(team1RookieStatuses, team2RookieStatuses));
    }
}
