package com.framework.utils.dataProviders;

import com.framework.utils.ExcelDocumentReader;

public class ExcelDataProvider {
	private String filepath;
	private String sheetName;
	private int row;

	public ExcelDataProvider(String filepath, String sheetName) {
		this.filepath = getClass().getResource(filepath).getPath();
		this.sheetName = sheetName;
		this.row = -1;
	}

	public ExcelDataProvider(String filepath, String sheetName, int rowToRead) {
		this.filepath = filepath;
		this.sheetName = sheetName;
		this.row = rowToRead;
	}

	public void setDatatablePath(String path) {
		this.filepath = path;
	}

	public void setDatatableSheet(String sheet) {
		this.sheetName = sheet;
	}

	public void setDatatableRow(int rowToRead) {
		this.row = rowToRead;
	}

	public Object[][] getTestData() {
		return new ExcelDocumentReader(this.filepath).readData(this.sheetName, this.row);
	}
}
