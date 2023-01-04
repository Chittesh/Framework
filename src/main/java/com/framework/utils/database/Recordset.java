package com.framework.utils.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

public class Recordset {
	private Logger logger = LogManager.getLogger(StringFormatterMessageFactory.INSTANCE);
	
	private static final String COLUMN_NAME_MESSAGE = "Column name cannot be blank. Column name entered was: ";
	private static final String START_ROW_MESSAGE = "Start row value needs to be 1 or greater. Start row entered was: ";
	private int columnRow = 0;
	private int startRow = 1;
	private int totalRows = 0;
	private int totalColumns = 0;
	private int currentRow = 1;
	private Object[][] rs = null;

	public Recordset(Object[][] rs) {
		this.rs = rs;
		this.totalRows = rs.length - 1;
		this.totalColumns = rs[columnRow].length;
	}

	public Object[][] getArray() {
		return rs;
	}

	public int getRowCount() {
		return totalRows;
	}

	public int getColumnCount() {
		return totalColumns;
	}

	public void moveFirst() {
		currentRow = startRow;
	}

	public void moveNext() {
		if (currentRow <= totalRows + 1)
			currentRow++;
	}

	public void movePrevious() {
		if (currentRow > startRow)
			currentRow--;
	}

	public void moveLast() {
		currentRow = totalRows;
	}

	public boolean isFirst() {
		return currentRow == startRow;
	}

	public boolean hasNext() {
		return currentRow <= totalRows;
	}

	public boolean isLast() {
		return currentRow > totalRows;
	}

	public int getColumnIndex(String name) {
		int index = 0;
		for (int columnIndex = 0; columnIndex < totalColumns; columnIndex++) {
			if (rs[columnRow][columnIndex].toString().equalsIgnoreCase(name)) {
				index = columnIndex + 1;
				break;
			}
		}
		return index;
	}

	public String getValue(String columnName) {
		int index = -1;
		
		if (columnName.isEmpty())
			throw new IllegalStateException(COLUMN_NAME_MESSAGE + columnName);
		
		index = getColumnIndex(columnName) - 1;
		
		if (index == -1)
			return ""; // Return blank value if no column found
		
		return rs[currentRow][index].toString();
	}

	public String getValue(String columnName, int row) {
		int index = -1;
		
		if (columnName.isEmpty())
			throw new IllegalStateException(COLUMN_NAME_MESSAGE + columnName);
		
		if (row < 0)
			throw new IllegalStateException(START_ROW_MESSAGE + row);
		
		index = getColumnIndex(columnName) - 1;
		
		if (index == -1)
			return ""; // Return blank value if no column found
		
		return rs[row][index].toString();
	}

	public String getValue(int column, int row) {
		if (column < 0)
			throw new IllegalStateException(START_ROW_MESSAGE + column);
		
		if (row < 0)
			throw new IllegalStateException(START_ROW_MESSAGE + row);
		
		return rs[row][--column].toString();
	}

	public void removeColumn(String columnName) {
		//to be implemented later
	}

	public String printString() {
		return PrettyPrinter.print(rs);
	}

	public void print() {
		logger.info(() -> PrettyPrinter.print(rs));
	}
}
