package com.framework.utils;

import java.io.File;
import java.util.Calendar;

public class Constants {
	
	private Constants() {
		
	}
	
    public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final int CURRENT_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    public static final int CURRENT_DAY = Calendar.getInstance().get(Calendar.DATE);
    
    public static final String REGRESSION_PREFIX = "RT-";
    public static final String AUTOMATION_PREFIX = "AT-";
    

    /** Default Docker Hub URL */
    static final String DOCKER_HUB = "http://10.62.234.22:4444/wd/hub";
    static final String VDI_HUB = "http://10.42.197.190:4444/wd/hub";

    /** Location of the environment URLs properties file */
    public static final String ENVIRONMENT_URL_PATH = "EnvironmentURLs";

    /** Location of the environment URLs properties file */
    public static final String SALESFORCE_DATAPROVIDER_PATH = "/allegis/salesforce/dataProviders/";

    public static final String SANDBOX_PATH = "/sandbox/";

    /** Location of drivers in project */
    public static final String DRIVERS_PATH_LOCAL = "/drivers/";
    public static final String DRIVERS_PATH_REMOTE = "C:\\Selenium\\WebDrivers\\";
    
    public static final String GECKO_DRIVER_PATH          =  DRIVERS_PATH_LOCAL + "geckodriver.exe";
    public static final String CHROME_WINDOWS_DRIVER_PATH =  DRIVERS_PATH_LOCAL + "chromedriver.exe";
    public static final String CHROME_MAC_DRIVER_PATH    =  DRIVERS_PATH_LOCAL + "mac/chromedriver";
    
    

    /** Location of tnsnames in project */
    public static final String TNSNAMES_PATH = "/database/";

    /** An alias for File.separator */
    public static final String DIR_SEPARATOR = File.separator;

    /** The current path of the project */
    public static final String CURRENT_DIR = determineCurrentPath();

    /** The location screenshots are to be stored */
    public static final String SCREENSHOT_FOLDER = CURRENT_DIR + "selenium-reports" + DIR_SEPARATOR + "html" + DIR_SEPARATOR + "screenshots";
    public static final String SCREENSHOT_FOLDERS_NAME = "selenium-reports" + DIR_SEPARATOR + "html" + DIR_SEPARATOR + "screenshots";

    /** Location of the user credentials properties file */
    public static final String USER_CREDENTIALS_PATH = "Credentials";

    /** Config property for framework JAR */
    public static final String CONFIG_PATH = "Config";

    /** The global system property line.separator */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    /** An alias for the global system property line.separator */
    public static final String NEW_LINE = LINE_SEPARATOR;

    public static final int DEFAULT_RETRY_COUNT = 0;

    public static final int DEFAULT_GLOBAL_DRIVER_TIMEOUT = 90;

    public static final int WEBDRIVER_TIMEOUT = 60;

    /**
     * The timeout (seconds) for finding web elements on a page, shouldn't be too long
     */

    public static final int ELEMENT_TIMEOUT = 30;

    public static final int SYNC_TIMEOUT = 30;

    public static final int MAIL_TIMEOUT = 180;

    /** The timeout (seconds) for page/DOM/transitions, should also be a generous */

    public static int PAGE_TIMEOUT = 90;

    /** The timeout (seconds) for page/DOM/transitions, should also be a generous */
    public static int SHORT_TIMEOUT = 10;

    /** The timeout (seconds) for page/DOM/transitions, should also be a generous */
    public static int VERY_SHORT_TIMEOUT = 2;

    public static double SHORTEST_TIMEOUT = .5;

    public static int LOOP_COUNT = 10;

    /** System properties */
    public static final String APPLICATION_UNDER_TEST = "selenium.applicationUnderTest";
    public static final String BROWSER = "selenium.browser";
    public static final String BROWSER_VERSION = "selenium.browserVersion";
    public static final String OPERATING_SYSTEM = "selenium.OS";
    public static final String RUN_LOCATION = "selenium.runLocation";
    public static final String SELENIUM_HUB_URL = "selenium.hubUrl";
    public static final String TEST_DRIVER_TIMEOUT = "selenium.testDriverTimeout";
    public static final String TEST_ENVIRONMENT = "selenium.testEnvironment";
    public static final String TEST_NAME = "selenium.testName";
    public static final String XML_FILES = "/xmls/";
    public static boolean defaultSyncHandler = true;
    public static int millisecondsToPollForElement = 250;

    /**
     * Set on how to handle element sync failures. True will cause the sync to throw
     * an exception while false will just have the element sync return a boolean
     *
     * @param syncHandler
     *            True/False
     * @version 1/14/2016
     */
    public static void setSyncToFailTest(boolean syncHandler) {
        defaultSyncHandler = syncHandler;
    }

    /**
     * Defaults to "./" if there's an exception of any sort.
     *
     * @warning Exceptions are swallowed.
     * @return Constants.DIR_SEPARATOR
     */
    final private static String determineCurrentPath() {
        try {
            return (new File(".").getCanonicalPath()) + Constants.DIR_SEPARATOR;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "." + Constants.DIR_SEPARATOR;
    }
}