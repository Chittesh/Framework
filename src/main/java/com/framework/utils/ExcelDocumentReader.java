package com.framework.utils;

import static com.framework.core.StreamUtils.streamOf;
import static com.framework.utils.utilities.ExcelUtils.withSheet;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;


public class ExcelDocumentReader {
	
	private String filepath;
	
	private DataFormatter formatter = new DataFormatter();
	private Map<CellType, Function<Cell, String>> cellMapper = new EnumMap<>(CellType.class);
	
	public ExcelDocumentReader(String filepath) {
		this.filepath = filepath;
		
		cellMapper.put(CellType.NUMERIC, formatter::formatCellValue);
		cellMapper.put(CellType.STRING, Cell::getStringCellValue);
		cellMapper.put(CellType.FORMULA, this::extractFormula);
		cellMapper.put(CellType.BOOLEAN, this::extractBoolean);
	}

	/**
	 * This gets the test data from excel workbook by the sheet specified. It
	 * returns all the data as a 2d array
	 * 
	 * @param sheetName
	 *            the excel sheet
	 * @version 10/16/2014
	 * @return 2d array of test data
	 */

	public Object[][] readData(String sheetName) {
		return (readData(sheetName, -1));
	}

	public Object[][] readData(String sheetName, int rowToRead) {

		return withSheet(filepath, sheetName, (sheet -> read(sheet, rowToRead)));
	}
	
	private Object[][] read(XSSFSheet workbook, int row) {
		
		long startRow = isReadAll(row) ? 1 : row;
		int totalRows = isReadAll(row) ? workbook.getLastRowNum() : 1;

		return streamOf(workbook.iterator())
			.skip(startRow)
			.limit(totalRows)
			.map(this::convertoRowToArray)
			.toArray(Object[][]::new);		
	}
	
	private Object[] convertoRowToArray(Row row) {
		Object[] rowArray = new Object[row.getLastCellNum()];
		for (int i = 0; i < rowArray.length; i++) {
			rowArray[i] = convert(row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
		}
		
		return rowArray;
	}

	private boolean isReadAll(int rowToRead) {
		return rowToRead == -1;
	}
	
	private String convert(Cell cell) {
		return cell != null && cellMapper.containsKey(cell.getCellType()) ? cellMapper.get(cell.getCellType()).apply(cell) : "";
	}

	private String extractBoolean(Cell cell) {
		return String.valueOf(cell.getBooleanCellValue());
	}

	private String extractFormula(Cell cell) {
		return String.valueOf(cell.getCellFormula());
	}

}