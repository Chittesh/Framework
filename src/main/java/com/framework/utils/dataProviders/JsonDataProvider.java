package com.framework.utils.dataProviders;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.framework.api.restServices.exceptions.RestException;
import com.framework.exception.automation.DataProviderInputFileException;
import com.framework.utils.io.FileLoader;

public class JsonDataProvider {

	private JsonDataProvider() {
		
	}
	
	/**
	 * The json read in must follow a specific format <br/>
	 * { <br/>
	 * &nbsp; "thisCanBeWhateverValueYouWant": [ <br/>
	 * &nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp; "iterationName": "Scenario 1", <br/>
	 * &nbsp;&nbsp;&nbsp; "data":[ <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 1 name", <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 1 value" <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; }, <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 2 name", <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 2 value" <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; } <br/>
	 * &nbsp;&nbsp;&nbsp; ] <br/>
	 * &nbsp;&nbsp; }, <br/>
	 * &nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp; "iterationName": "Scenario 2", <br/>
	 * &nbsp;&nbsp;&nbsp; "data":[ <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 1 name", <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 1 value" <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; }, <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; { <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "Parameter 2 name", <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "value": "Parameter 2 value" <br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp; } <br/>
	 * &nbsp;&nbsp;&nbsp; ] <br/>
	 * &nbsp;&nbsp; }
	 * 
	 * Additionally you can have multiple nodes, for different test scenarios.
	 * 
	 * @param filePath
	 *            filepath starting from src/main/resources
	 * @return Object[][] for the dataprovider
	 */
	public static Object[][] getData(String filePath, String scenarioName) {

		JSONArray testData = null;
		JSONArray data;
		
		int rows = -1;
		
		try {
			String json = FileLoader.loadFileFromProjectAsString(filePath);
			testData = new JSONObject(json).getJSONArray(scenarioName);
			rows = testData.length();
			data = (JSONArray) testData.getJSONObject(0).get("data");
		} catch (IOException ioe) {
			throw new RestException("Failed to read json file", ioe);
		}
		catch (JSONException e) {
			throw new DataProviderInputFileException("No JSON object was found containing name: [" + scenarioName + "]");
		}

		int columns = data.length() + 1;

		Object[][] dataArray = new String[rows][columns];

		for (int rowNum = 0; rowNum < rows; rowNum++) {
			try {
				String iterationName = testData.getJSONObject(rowNum).get("iterationName").toString();
				dataArray[rowNum][0] = iterationName;
			} catch (JSONException e2) {
				throw new DataProviderInputFileException(
						"TestData iteration [ " + (rowNum + 1) + " ] is missing it's [ iterationName ] object");
			}

			for (int colNum = 1; colNum < columns; colNum++) {
				try {
					int iterationColumnLength = ((JSONArray) testData.getJSONObject(0).get("data")).length();
					if (columns != iterationColumnLength + 1)
						throw new DataProviderInputFileException("Number of parameters for iteration was [ " + iterationColumnLength + " ] instead of [ "
																 + (columns - 1) + " ]");
					
					String parameterValue = ((JSONArray) testData.getJSONObject(rowNum).get("data")).getJSONObject(colNum - 1)
							.get("value").toString();
					
					dataArray[rowNum][colNum] = parameterValue;
				} catch (JSONException e) {
					throw new DataProviderInputFileException("Failed to find value");
				}
			}
		}
		return dataArray;
	}
	
}