package com.framework.utils.utilities;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private static final String MASTER_PATH = new File(System.getProperty("user.dir"), "MasterSpreadsheet2017.xlsx").getPath();
	
	private static Map<String, Workbook> workbooktable = new HashMap<>();

	private static final Logger LOGGER = LogManager.getLogger(ExcelUtils.class);
	
	private final DataFormatter dataFormatter = new DataFormatter();
	
	/**
	 * To get the excel sheet workbook
	 */
	public static Workbook getWorkbook(String path) {
		if (workbooktable.containsKey(path)) {
			return workbooktable.get(path);
		}
		
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			workbooktable.put(path, workbook);
			return workbook;

		} catch (Exception e) {
			LOGGER.error("Failet to get workbook", e);
			return rethrow(e);
		}
	}

	/**
	 * Get the total rows present in excel sheet
	 */
	public static int getRows(String testSheetName, String pathOfFile) {
		
		return getSheet(testSheetName, pathOfFile)
				.map(Sheet::getLastRowNum)
				.orElse(0);
	}

	/**
	 * Get the total columns inside excel sheet
	 */
	public static int getColumns(String testSheetName, String pathOfFile) {
		return getSheet(testSheetName, pathOfFile)
				.map(sheet -> sheet.getRow(0))
				.map(Row::getLastCellNum)
				.orElse((short) 0);
	}

	/**
	 * Read the content of the cell
	 */
	public String readCell(int rowNum, int colNum, String sheetName, String pathOfFile) {
		
		return ofNullable(getWorkbook(pathOfFile))
			.map(workbook -> workbook.getSheet(sheetName))
			.map(sheet -> sheet.getRow(rowNum))
			.map(row -> row.getCell(colNum))
			.map(dataFormatter::formatCellValue)
			.orElse(null);
	}

	/**
	 * Write the content to the cell
	 * 
	 * @throws IOException
	 */
	public void writeToCell(int rowNum, int colNum, String sheetName, String pathOfFile, String textToWrite) {
		
		withSheet(pathOfFile, sheetName, sheet -> writeToCell(sheet, rowNum, colNum, textToWrite) );
	}
	
	public static <R> R withSheet(String path, String sheetName, Function<XSSFSheet, R> function) {
		try (FileInputStream excelFile = new FileInputStream(path);
				XSSFWorkbook excelWBook = new XSSFWorkbook(excelFile) ) {

			XSSFSheet excelWSheet = excelWBook.getSheet(sheetName);
			Validate.notNull(excelWSheet, "Refered sheet named [%s] does not exist in [%s].", sheetName, path);

			return function.apply(excelWSheet);
			
		} catch (IOException e) {  
			LOGGER.error("Could not read the Excel sheet", e);
			return rethrow(e);
		} 
	}

	public String readExcelCell(int row, int col) {
		return readCell(row, col, "vms", MASTER_PATH);
	}

	public void writeToExcelCell(int row, int col, String text) {
		writeToCell(row, col, "vms", MASTER_PATH, text);
	}
	
	private static Optional<Sheet> getSheet(String testSheetName, String pathOfFile) {
		return ofNullable(getWorkbook(pathOfFile)
				.getSheet(testSheetName));
	}
	
	
	private Cell writeToCell(XSSFSheet sheet, int row, int column, String text) {

		// Retrieve the row and check for null
		XSSFRow sheetrow = ofNullable(sheet.getRow(row))
								.orElse(sheet.createRow(row));
		
		// Update the value of cell
		Cell cell = ofNullable(sheetrow.getCell(column))
								.orElse(sheetrow.createCell(column));
		
		cell.setCellValue(text);
		return cell;
	}
}