package com.framework.utils.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import com.framework.utils.database.Database;
import com.framework.utils.database.Recordset;
import com.framework.utils.database.databaseImpl.MySQLDatabase;
import com.framework.utils.database.databaseImpl.OracleDatabase;


@SuppressWarnings("unused")
public class DatabaseDataProvider {
	private Logger logger = LogManager.getLogger(StringFormatterMessageFactory.INSTANCE);
	
	private String databaseType = "";
	private static String host = "10.238.242.55";
	private static String port = "3306";
	private static String database = "selenium";
	private static String username = "selenium_ro";
	private static String password = "selenium_ro";
	public static final String DB2 = "db2";
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	Database db = null;

	public DatabaseDataProvider(String databaseType) {
		this.databaseType = databaseType;
		
		switch (databaseType) {
			case MYSQL:
				db = new MySQLDatabase(host, port, database);
				break;
			
			case ORACLE:
				db = new OracleDatabase(host, port, database);
				break;
				
			default:
				throw new IllegalArgumentException("unknown database type - " + databaseType);
			
		}

		db.setDbUserName(username);
		db.setDbPassword(password);
	}

	public Object[][] getTestData(String testName) {
		return getTestData(testName, "0");
	}

	public Object[][] getTestData(String testName, int scenario) {
		return getTestData(testName, String.valueOf(scenario));
	}

	public Object[][] getTestData(String testName, String scenario) {
		return queryTestDataTable(testName, scenario);
	}

	private Object[][] queryTestDataTable(String testName, String scenario) {
		String filterScenario = "";
		/*
		 * Allow option to pull a specific scenario. If "0" is used, then do not filter
		 * on SCENARIO_NUM to pull all scenarios
		 */
		if (!scenario.equals("0"))
			filterScenario += " AND SCENARIO_NUM  = " + scenario;
		String metaSql = "SELECT MAX(Scenario_Num) NUMROWS,  MAX(Data_Field_Order) NUMCOLUMNS  FROM justin.testdata WHERE TEST_NAME= '"
				+ testName + "'" + filterScenario;
		String sql = "SELECT DATA_FIELD_NAME, DATA_FIELD_VALUE FROM justin.testdata WHERE TEST_NAME= '" + testName
				+ "' " + filterScenario + " ORDER BY SCENARIO_NUM, DATA_FIELD_ORDER";
		;

		Recordset metaRs = new Recordset(db.getResultSet(metaSql));
		/*
		 * Allow option to pull a specific scenario. If "0" is not used, then only one
		 * scenario is run
		 */
		int numberRows = 1;
		if (scenario.equals("0"))
			numberRows = Integer.parseInt(metaRs.getValue("NUMROWS"));

		int numberColumns = Integer.parseInt(metaRs.getValue("NUMCOLUMNS"));

		return transformDataObject(db.getResultSet(sql), numberRows, numberColumns);
	}

	private Object[][] transformDataObject(Object[][] oldData, int numberRows, int numberColumns) {
		Object[][] results = new Object[numberRows][numberColumns];
		int rowOffset = 0;
		int columnOffset = 0;

		for (int x = 0; x < oldData.length - 1; x++) {
			for (int y = 0; y < oldData[x].length - 1; y++) {
				if (x % numberColumns == 0 && x != 0) {
					columnOffset += numberColumns;
					rowOffset++;
					logger.info("%n");
				}
				
				logger.info("%1$s : %2$s", oldData[x + 1][0], oldData[x + 1][1]);
				
				results[y + rowOffset][x - columnOffset] = oldData[x + 1][1];
			}
		}
		return results;
	}
}
