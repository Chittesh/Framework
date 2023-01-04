package com.framework.utils;

import static com.framework.utils.BrowserTypes.*;
import static com.framework.utils.Constants.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.framework.exception.AutomationException;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

	private TestEnvironmentExecutionContext context;
	
	private String browser;
	private String os;

	public DriverManager(TestEnvironmentExecutionContext context) {
		this.context = context;
		
		this.browser     = context.getBrowserUnderTest().toLowerCase();
		this.os          = context.getOperatingSystem();
	}
	
	public AllegisDriver setupLocalDriver() throws Exception {
	    AllegisDriver driver = null;
	    
	    switch (browser) {
	    
	    	case FIREFOX:
	    		
	    		WebDriverManager.firefoxdriver().setup();
	    		
	    	   	driver = new AllegisDriver(optionsFactory().forFirefox());
	    	   	break;

	    	case CHROME:
	    	case CHROMEMOBILEEMULATOR:
	    		
	    		if ("mac".equalsIgnoreCase(os)) {
	    			WebDriverManager.chromedriver().mac().setup();
	    			
	    			startChromeProcessOnMac(os);
	    		}
	    		else {
	    			WebDriverManager.chromedriver().win().setup();
	    		}
	    		
	    		driver = new AllegisDriver((ChromeOptions)optionsFactory().createOptionsFor(browser));
	    		break;
	    		
	    	default:
	    		throw new AutomationException("invalid browser - " + browser);
	    		
	    }

	    setTimeouts(driver);
	    resizeBrowser(driver);
	    
	    return driver;
	}

	public AllegisDriver setupDockerGridDriver(URL remoteUrl) {
		
		RetryCommand retryCommand = new RetryCommand(5, 10);
		RemoteWebDriver drv = retryCommand.execute(() -> createRemoteDriver(remoteUrl));
		
   	    AllegisDriver driver = new AllegisDriver(drv);
		    			
		setTimeouts(driver);
		resizeBrowser(driver);
		
		return driver;
		
	}
	
	private RemoteWebDriver createRemoteDriver(URL remoteUrl) {
		WebDriverManager driverManager = null;
		
		switch (browser.toLowerCase()) {
			case CHROME:
				driverManager = WebDriverManager.chromedriver(); 				
				break;
				
			case FIREFOX:
				driverManager = WebDriverManager.firefoxdriver();
				break;
				
			case CHROMEMOBILEEMULATOR:
				driverManager = WebDriverManager.chromedriver().capabilities(capabilitiesFactory().forChromeInEmulationMode());
				break;
				
			default:
				throw new AutomationException("invalid browser - " + browser);
		}
		
		RemoteWebDriver drv = (RemoteWebDriver) driverManager.remoteAddress(remoteUrl).create();
	   	drv.setFileDetector(new LocalFileDetector());
	   	
	   	return drv;
	}
	
	public AllegisDriver setupBrowserStackDriver(URL remoteUrl) {
		DesiredCapabilities dc = capabilitiesFactory().fromJson();

		AllegisDriver driver = new AllegisDriver(dc, remoteUrl);
		    			
		setTimeouts(driver);
		resizeBrowser(driver);
		
		return driver;
	}
	
	private void startChromeProcessOnMac(String os) throws IOException, InterruptedException {
		if (!"mac".equalsIgnoreCase(os)) return;
		
		File file = new DriverFile().getFile(CHROME_MAC_DRIVER_PATH);
		Process proc = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "chmod 777 " + file.getAbsolutePath() });		        
		proc.waitFor();
	}
	
	public AllegisDriver setupLocalAppiumDriver(String deviceName, String nativeApp, URL url) {    			
		String userDir = System.getProperty("user.dir");
		
		UiAutomator2Options options = optionsFactory().uiAutomator2Options(deviceName);
		options.setApp(userDir + "/src/main/resources/" + nativeApp);
		
		RemoteWebDriver driver = new AndroidDriver(url, options);
		
		return new AllegisDriver(driver);
	}
	
	public AllegisDriver setupRemoteAppiumDriver(String deviceName, URL url) {
		String appiumAppHash = context.getAppiumAppHash();
		
		UiAutomator2Options options = optionsFactory().uiAutomator2Options(deviceName);
		options.setCapability("app", appiumAppHash);
		
		RemoteWebDriver driver = driverFromId(url, options);
		
		return new AllegisDriver(driver);
	}
	
	private OptionsFactory optionsFactory() {
		return new OptionsFactory(context);
	}
	
	private CapabilitiesFactory capabilitiesFactory() {
		return new CapabilitiesFactory(context);
	}
	
	private void setTimeouts(AllegisDriver driver) {    	    	
	   	if (Boolean.parseBoolean(context.getAllowImplicitWait()))
	   		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);               
	}
	
	private void resizeBrowser(AllegisDriver driver) {
		String size = context.getBrowserSize();
		driver.resizeBrowser(size);
	}
	
	private RemoteWebDriver driverFromId(URL url, UiAutomator2Options caps) {
		return context.getDeviceId().equalsIgnoreCase("android") ? new AndroidDriver(url, caps)
																 : new IOSDriver(url, caps);
	}
	
	public void quit(AllegisDriver driver) {		    	
    	if(driver != null ) 
    		driver.quit();
	}
	
}
