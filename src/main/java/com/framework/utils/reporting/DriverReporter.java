package com.framework.utils.reporting;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import static com.framework.utils.Constants.*;
import com.framework.utils.TestReporter;
import com.framework.utils.utilities.StackTraceInfo;

public class DriverReporter {
	
	private DriverReporter() {
	}
	
	public static void logScreenshot(WebDriver driver) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	logScreenshot("other", driver);
    }

    public static void logScreenshot(String testClassName, WebDriver driver) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	String destDir = String.format("%s%s%s%s", SCREENSHOT_FOLDER, DIR_SEPARATOR, testClassName, DIR_SEPARATOR);
        new File(destDir).mkdirs();
    
        String fileName     = UUID.randomUUID() + ".png";
        String fileLocation = destDir + fileName;
        
        TestReporter.log("Screenshot location: " + fileLocation);
        
        try {
            FileUtils.writeByteArrayToFile(new File(fileLocation), ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));

            if (isOnJenkins()) {
                String webFileLocation = webDir(testClassName ) + fileName;
                Reporter.log("<a  target='_blank' href='" + webFileLocation + "'><img src='" + webFileLocation + "' height='200' width='300'/></a>");
            } else {
                Reporter.log("<a  target='_blank' href='" + fileLocation + "'> <img src='file:///" + fileLocation + "' height='200' width='300'/> </a>");
            }
        } catch (Exception e) {
            TestNgReporter.print("Could not create the screenshot file - Exception: ");
            e.printStackTrace();
            
            Reporter.log(String.format("<a  target='_blank' href='%1$s'>%1$s</a>", isOnJenkins() ? webDir(testClassName) : destDir ));
        }
    }
    
    public static String webDir( String testName ) {
    	return String.format("%sws/%s/%s/", jobURL(), com.framework.utils.Constants.SCREENSHOT_FOLDERS_NAME, testName);
    }
    
	public static boolean isOnJenkins() {
		String jobURL = jobURL();
		return jobURL != null && !jobURL.isEmpty();
	}
	
	public static String jobURL() {
		return System.getenv("JOB_URL");
	}

	public static String analyzeLog(AllegisDriver driver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		
		StringBuilder builder = new StringBuilder();
		
		for (LogEntry entry : logEntries) {
			String entryInfo = new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage();
			builder.append(entryInfo);
		}
		
		return builder.toString();
	}

	public static void logConsoleErrors(AllegisDriver driver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (driver == null) {
			TestReporter.log("Driver was null, could not capture console errors");
			throw new AutomationException("driver is null!");
		}

		if (driver.getSessionId() == null) {
			Reporter.log("Session ID is null, cannot log console errors");
			throw new AutomationException("session is null!");
		}

		if (driver.getDriverCapability().browserName().equalsIgnoreCase("chrome")) {
			try {
				Reporter.log("<br/><b><font size = 4> Console errors: </font></b><br/>");
				List<LogEntry> logList = driver.manage().logs().get("browser").getAll();
				String color = "red";

				if (logList.isEmpty())
					Reporter.log("NO ERRORS");

				for (LogEntry entry : logList) 
					if (entry.getLevel() == Level.SEVERE) {
						Reporter.log(" <font size = 2 color=\"" + color + "\"><b> Level :: " + entry.getLevel().getName() + "</font></b><br />");
						Reporter.log(" <font size = 2 color=\"" + color + "\"><b> Message :: " + entry.getMessage() + "</font></b><br />");
					}
			} catch (Exception e) {
				Reporter.log("Could not grab the console logs: " + e.getMessage());
			}


		}
	}
	
}
