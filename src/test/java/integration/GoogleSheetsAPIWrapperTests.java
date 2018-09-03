package test.java.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;

import com.google.api.services.sheets.v4.model.ValueRange;

import main.java.GoogleSheetsAPIWrapper;
import main.java.SecurityInfoXmlParser;

public class GoogleSheetsAPIWrapperTests {

	// MiLB Rosters Spreadsheet Data
	private static String mSpreadsheetId;
	private static String mApiKey;
	private static String mClientSecretRelativeFilePath;
	private List<String> mSingleRange1 = new ArrayList<>(Arrays.asList("Sheet2!E9:F11"));
	private List<String> mMultipleRanges1 = new ArrayList<>(Arrays.asList("Sheet2!E9:F9", "Sheet2!E10:F10", "Sheet2!E11:F11"));
	private String mSingleRange2 = "Sheet2!E15:F17";
	private List<String> mMultipleRanges2 = new ArrayList<>(Arrays.asList("Sheet2!E15:F15", "Sheet2!E16:F16", "Sheet2!E17:F17"));
	
	private static String cell1;
	private static String cell2;
	private static String cell3;
	private static String cell4;
	private static String cell5;
	private static String cell6;
	
	@Before
	public void setUp() {
		cell1 = UUID.randomUUID().toString();
		cell2 = UUID.randomUUID().toString();
		cell3 = UUID.randomUUID().toString();
		cell4 = UUID.randomUUID().toString();
		cell5 = UUID.randomUUID().toString();
		cell6 = UUID.randomUUID().toString();
	}
	
	@BeforeClass
	public static void oneTimeSetUp() throws SAXException, IOException, ParserConfigurationException {
		SecurityInfoXmlParser parser = new SecurityInfoXmlParser("milb_roster_spreadsheet_security_info.xml");
		parser.OpenXmlFile();
		mSpreadsheetId = parser.GetSpreadsheetId();
		mApiKey = parser.GetApiKey();
		mClientSecretRelativeFilePath = parser.GetClientSecretFilePath();
		
	}

	@Test
	public void testUpdateBatchValuesSingleRange() throws IOException, GeneralSecurityException {
		
		List<List<Object>> expectedValues = new ArrayList<>(Arrays.asList(
			new ArrayList<>(Arrays.asList(cell1, cell2)), // row-major order
			new ArrayList<>(Arrays.asList(cell3, cell4)),
			new ArrayList<>(Arrays.asList(cell5, cell6))
		));
		
		List<ValueRange> data = new ArrayList<>(Arrays.asList(new ValueRange()));
		data.get(0).setValues(expectedValues);
		data.get(0).setRange(mSingleRange2);
		
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
		wrapper.UpdateBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, data);
		
		List<List<Object>> actualValues = 
				wrapper.GetBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, new ArrayList<>(Arrays.asList(mSingleRange2)))
				.getValueRanges()
				.get(0)
				.getValues();
		
		assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void testUpdateBatchValuesMultipleRanges() throws IOException, GeneralSecurityException {
		List<List<List<Object>>> expectedValues = new ArrayList<>(Arrays.asList(
			Arrays.asList(new ArrayList<>(Arrays.asList(cell1, cell2))), // row-major order
			Arrays.asList(new ArrayList<>(Arrays.asList(cell3, cell4))),
			Arrays.asList(new ArrayList<>(Arrays.asList(cell5, cell6)))
		));
		
		List<ValueRange> data = new ArrayList<>();
		for (int i = 0; i < expectedValues.size(); i++) {
			ValueRange v = new ValueRange();
			v.setValues(expectedValues.get(i));
			v.setRange(mMultipleRanges2.get(i));
			data.add(v);
		}
		
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
		wrapper.UpdateBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, data);
		
		List<List<List<Object>>> actualValuesList = new ArrayList<>();
		List<ValueRange> valueRanges = wrapper.GetBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, mMultipleRanges2)
				.getValueRanges();
		for (ValueRange v : valueRanges) 
			actualValuesList.add(v.getValues());
		
		assertEquals(expectedValues, actualValuesList);
	}
	
	@Test
	public void testGetBatchValuesSingleRange() throws IOException, GeneralSecurityException {
		List<List<Object>> expectedValues = new ArrayList<>(Arrays.asList(
			new ArrayList<>(Arrays.asList("test", "data")), // row-major order
			new ArrayList<>(Arrays.asList("24%1", "TRUE")),
			new ArrayList<>(Arrays.asList("asd asd", "FALSE"))
		));
		
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
		
		List<List<Object>> actualValues = 
				wrapper.GetBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, mSingleRange1)
				.getValueRanges()
				.get(0)
				.getValues();
		
		assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void testGetBatchValuesMultipleRanges() throws IOException, GeneralSecurityException {
		List<List<List<Object>>> expectedValues = new ArrayList<>(Arrays.asList(
			Arrays.asList(new ArrayList<>(Arrays.asList("test", "data"))), // row-major order
			Arrays.asList(new ArrayList<>(Arrays.asList("24%1", "TRUE"))),
			Arrays.asList(new ArrayList<>(Arrays.asList("asd asd", "FALSE")))
		));
		
		GoogleSheetsAPIWrapper wrapper = new GoogleSheetsAPIWrapper();
	
		
		List<List<List<Object>>> actualValuesList = new ArrayList<>();
		List<ValueRange> valueRanges = wrapper.GetBatchValues(mSpreadsheetId, mApiKey, mClientSecretRelativeFilePath, mMultipleRanges1)
				.getValueRanges();
		for (ValueRange v : valueRanges) 
			actualValuesList.add(v.getValues());
		
		assertEquals(expectedValues, actualValuesList);
	}

}
