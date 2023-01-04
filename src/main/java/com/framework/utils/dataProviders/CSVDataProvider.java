package com.framework.utils.dataProviders;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CSVDataProvider {

	private static final Logger LOGGER = LogManager.getLogger(CSVDataProvider.class);

	private CSVDataProvider() {
		// Prevent pure static class from being instantiated.
	}

	/**
	 * This gets the test data from a csv file. It returns all the data as a 2D
	 * array
	 * 
	 * @param filePath the file path of the CSV file
	 * @version 12/18/2014
	 * @return 2d array of test data
	 */
	public static Object[][] getTestScenarioData(String filePath) {

		return getDocumentStream(filePath).toArray(Object[][]::new);
	}

	private static Stream<Object[]> getDocumentStream(String filePath) {

		return getRecordsStream(filePath).map(CSVDataProvider::recordToArray);
	}

	private static Stream<CSVRecord> getRecordsStream(String filePath) {
		if (!filePath.contains(":")) {
			filePath = CSVDataProvider.class.getResource(filePath).getPath();
		}

		filePath = filePath.replace("%20", " ");

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			return CSVParser.parse(br, CSVFormat.DEFAULT.withHeader()).getRecords().stream();

		} catch (IOException e) {
			LOGGER.error("Failed to read CSV file.", e);
			return rethrow(e);
		}
	}

	private static Object[] recordToArray(CSVRecord csvRecord) {
		return StreamSupport.stream(csvRecord.spliterator(), false).toArray();
	}

	/*
	 * This method gets the test data from a csv file based on Decision column
	 * value. If value is YES, the row is returned, else not returned. It returns
	 * all the data as a 2D array.
	 * 
	 * @param filePath
	 * 
	 * @param decisionColumnIndex
	 * 
	 * @return 2d array of test data
	 */
	public static Object[][] getDataByDecision(Stream<CSVRecord> recordStream, String decisionHeader) {

		return recordStream.filter(row -> Pattern.matches("(?i)^yes\\b", row.get(decisionHeader)))
				.map(CSVDataProvider::recordToArray).toArray(Object[][]::new);
	}
	
	/*
	 * This method sorts a column data by header name. 
	 * Sorts the column data in increasing order.
	 * 
	 * @param filePath
	 * @param headerName
	 * 
	 * @return Stream of CSVRecord
	 */
	public static Stream<CSVRecord> sortDataByHeader(String filePath, String headerName) {

		Validate.notEmpty(headerName, "Failed to read header name to sort the data.");
		
		return  getRecordsStream(filePath).sorted(new CSVRecordComparator(headerName));

	}
}
