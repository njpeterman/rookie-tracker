package rookie_tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsAPIWrapper {
	
	private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

    /* Get Batch Values
	 * Updates a batch of values from a google spreadsheet
	 * @param spreadsheetId, a string. Can be found in spreadsheet URL
	 * @param apiKey, a string. Obtained through Google Credentials
	 * @param clientSecretRelativeFilePath, a string. Relative to the 
	 * top level directory of the project. Client secret is a JSON file
	 * that is also obtained from Google Credentials. See the following for 
	 * more info: https://developers.google.com/sheets/api/guides/authorizing#APIKey
	 * @param data, a list of ValueRange objects, corresponding to the data to be entered at the ranges
	 * Note: ranges are included in the ValueRange objects.
	 * specified by the parameter 'ranges'
	 */
    public void UpdateBatchValues
    (String spreadsheetId, String apiKey, String clientSecretRelativeFilePath, List<ValueRange> data) throws IOException, GeneralSecurityException
    {
        // TODO: Assign values to desired fields of `requestBody`:
        BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest();
        requestBody.setData(data);
        requestBody.setValueInputOption("RAW");

        Sheets sheetsService = createSheetsService(clientSecretRelativeFilePath);
        Sheets.Spreadsheets.Values.BatchUpdate request =
            sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, requestBody);

        request.execute();
    }
    
	/* Get Batch Values
	 * Returns a batch of values from a google spreadsheet
	 * @param spreadsheetId, a string. Can be found in spreadsheet URL
	 * @param apiKey, a string. Obtained through Google Credentials
	 * @param clientSecretRelativeFilePath, a string. Relative to the 
	 * top level directory of the project. Client secret is a JSON file
	 * that is also obtained from Google Credentials. See the following for 
	 * more info: https://developers.google.com/sheets/api/guides/authorizing#APIKey
	 * @param ranges, a list of strings. Example range - "C5:G7"
	 * @returns a BatchGetValuesResponse object, which can be converted 
	 * to a list of strings
	 */
	public BatchGetValuesResponse GetBatchValues
	(String spreadsheetId, String apiKey, String clientSecretRelativeFilePath, List<String> ranges) 
			throws IOException, GeneralSecurityException {
		
		Sheets sheetsService = createSheetsService(clientSecretRelativeFilePath);
		
	    Sheets.Spreadsheets.Values.BatchGet request = 
	    		getBatchRequest(sheetsService, spreadsheetId, apiKey, ranges);
	    
	    return request.execute();
	  }
	
	private Sheets.Spreadsheets.Values.BatchGet getBatchRequest(
			Sheets sheetsService, String spreadsheetId, String apiKey, List<String> ranges) throws IOException 
	{
		Sheets.Spreadsheets.Values.BatchGet 
			request = sheetsService.spreadsheets().values().batchGet(spreadsheetId);
		
		    request.setKey(apiKey);
		    request.setRanges(ranges);    
		    return request;
	}

	private Sheets createSheetsService(String clientSecretRelativeFilePath) throws IOException, GeneralSecurityException {
	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

	    InputStream in = (InputStream) new FileInputStream(new File(new File("").getAbsolutePath().concat("/" + clientSecretRelativeFilePath)));
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

	    // Build flow and trigger user authorization request.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	            httpTransport, jsonFactory, clientSecrets, SCOPES)
	            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
	            .setAccessType("offline")
	            .build();
	    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	    
	    return new Sheets.Builder(httpTransport, jsonFactory, credential).build();
	  }
}
