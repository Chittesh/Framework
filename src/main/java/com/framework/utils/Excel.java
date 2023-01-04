package com.framework.utils;

import java.io.File;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

@SuppressWarnings("unused")
public class Excel {

	private String filePath;
	private String sheetName;
	private Workbook wb;
	private Sheet sh;

	public Excel(String filePath) {
		this.filePath = filePath;

		try {
			wb = WorkbookFactory.create(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sh = wb.getSheetAt(0);
	}

	public String getCellString(int cellrow, int cellcol) {
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		CellReference cellReference = new CellReference(cellrow, cellcol);
		Row row = sh.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		return cell.getStringCellValue();
	}

	public Excel(String filePath, String sheetName) {
		this.filePath = filePath;
		this.sheetName = sheetName;
	}
}