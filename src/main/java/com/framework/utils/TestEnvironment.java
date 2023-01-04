package com.framework.utils;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.allegis.repository.browserstack.BrowserStackAPI;
import com.allegis.repository.browserstack.BrowserStackAppManager;
import com.framework.exception.AutomationException;
import com.framework.utils.sonarclient.SonarApi;
import com.framework.utils.sonarclient.SonarMetrics;
import com.framework.utils.utilities.PreambleRtNumberExtractor;

import static com.framework.utils.RunLocations.*;

import io.qameta.allure.Attachment;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.percy.selenium.Percy;

import com.allegis.service.frameworklog.FrameworkAuditService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestEnvironment extends BaseTest {		

	private Logger logger = LogManager.getLogger(StringFormatterMessageFactory.INSTANCE);
    
	private List<String> extensions = new ArrayList<>();
	
	@Inject
	public PerformanceMetricsLogger metricsLogger;
	
	@Inject
	public PageSpeedMetricsLogger pageSpeedLogger;
	
	@Inject
	public SonarScanMetricsLogger sonarMetricsLogger;
	
	@Inject
	private RemoteUrlProvider remoteUrlProvider;
	
	@Inject
	private SonarApiCredentialsProvider sonarApiCredentialsProvider;

	
    private static ThreadLocal<AllegisDriver> threadedDriver = new ThreadLocal<>();
    protected ThreadLocal<String> sessionId                  = new ThreadLocal<>();
    
    static List<String> tCs_lst= new ArrayList<String>();
    
    protected ResourceBundle appURLRepository = ResourceBundle.getBundle(Constants.ENVIRONMENT_URL_PATH);
   
    ResourceBundle configProp = ResourceBundle.getBundle(Constants.CONFIG_PATH);
    
	@Inject
	private BrowserStackAppManager browserStackAppManager;

	@Inject
	private BrowserStackAPI browserStackApi;
	
	@Inject
	private FrameworkAuditService auditService;
	
    private AppiumDriverLocalService service;

    public TestEnvironment() {
    	java.util.Optional.ofNullable(this.getClass().getAnnotation(ChromeExtensions.class))
    		.map(ChromeExtensions::value)
    		.ifPresent(available -> this.extensions = Arrays.asList(available));
    }

	@BeforeClass(alwaysRun=true)
	public  void startAppiumService() { 	
    	if (getRunLocation().equalsIgnoreCase(SIMULATOR)) {
    		service = new AppiumServiceBuilder()
    			       .withIPAddress("127.0.0.1")
    			       .usingAnyFreePort()
    			       .build();
    		
    		service.start();
    	}
	}
	
	@BeforeSuite(alwaysRun=true)
	public  void hasLocalPomContent() { 	
    	
		String message = "\n\n   Your project uses its own Maven %1$s instead of the framework ones!" +
						 "\n   We recommend using only the Allegis framework %1$s so that they are kept up to date." +
						 "\n   If the required %1$s are not present under Allegis Framework, please reach out to us." +
						 "\n   Please contact Amandeep Sooch to learn how to add new %1$s to framework.\n\n";
		
		String elements = "";
		
    	if (Boolean.TRUE.equals(PomInfo.hasDependencies())) 
    		elements += "DEPENDENCIES/";
    	
    	if (Boolean.TRUE.equals(PomInfo.hasProfileDependencies())) 
    		elements += "PROFILE DEPENDENCIES/";
    	
    	if (Boolean.TRUE.equals(PomInfo.hasPlugins())) 
    		elements += "PLUGINS/";

    	if (Boolean.TRUE.equals(PomInfo.hasProfilePlugins())) 
    		elements += "PROFILE PLUGINS/";
    		
    	if (Boolean.FALSE.equals(elements.isEmpty()))
    		logger.error(message, elements);
    	
    }

	@AfterClass(alwaysRun=true)	
	public void stopAppiumService() { 
    	if (getRunLocation().equalsIgnoreCase(SIMULATOR) && service != null) 
    		service.stop();
	}

	@AfterClass(alwaysRun=true)	
	public void cleanUp() throws IOException { 
		new Folder(".").deleteFiles("ajcore", ".txt");
	}
	
	@AfterSuite(alwaysRun=true)
	public void exportSonarMetricsToNewRelic() {
		
		Boolean exportToNewRelic = Boolean.parseBoolean(getContext().getSonarScanExportToNewRelic());
		
		if (Boolean.FALSE.equals(exportToNewRelic)) 
			return;
		
		String branchName = getBranchName();
			
		SonarMetrics sonarMetrics = new SonarApi(getSonarQubeHostName(), getSonarQubeAuthorizationKey())
										.getMetrics(PomInfo.getProjectId());
			
		sonarMetrics.setBranchName(branchName);
			
		sonarMetricsLogger.log(sonarMetrics);
		
	}
	
	public String getSonarQubeAuthorizationKey() {
		return sonarApiCredentialsProvider.getCodeScanAuthorization();
	}
	
	public String getSonarQubeHostName() {
		return sonarApiCredentialsProvider.getCodeScanHostName();
	}
	
	private String getBranchName() {
		try {
			String branchName = Files.readString(Path.of(".git/HEAD"), StandardCharsets.US_ASCII);
			
			int i = branchName.lastIndexOf("/");
			
			branchName = branchName.substring(i + 1);
			
			return branchName;
		} catch (IOException e) {
			throw new AutomationException("could not read the branch name from the .git/HEAD file!");
		}
	}
	
    private URL getServiceUrl () {
        return service.getUrl();
    }

    
    private Percy percy;
    
    @BeforeMethod
    public void setPercy() {
    	if (!Boolean.parseBoolean(getPercyStatus()) || getRunLocation().equalsIgnoreCase(API))
    	   return;
       
       this.percy = getAllegisDriver().map(Percy::new).orElseThrow(()-> new AutomationException("There is no driver available to create percy"));
    }
    
    public Percy getPercy() {
    	if (getRunLocation().equalsIgnoreCase(API))
       	   throw new AutomationException("cannot get PERCY driver if run location = API");
    	
    	if (!Boolean.parseBoolean(getPercyStatus()))
           throw new AutomationException("PERCY status = false");
    	  
    	return this.percy;
    }
    
    public void takeSnapshot(String pageName) {
    	this.percy.snapshot(String.format("%s test method -> %s snapshot", this.testMethodName, pageName));
    }
    
    
    private String testMethodName = "";
    
    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Method testMethod) throws IOException {
       
       auditService.reset();
       
       auditService.setTestMethodName(testMethod.getName());
       auditService.setTestClassName(testMethod.getDeclaringClass().getName());
       auditService.setArtifactId(PomInfo.getArtifactId());
       auditService.setFrameworkVersion(getContext().getFrameworkVersion());
       auditService.setXmlParameters(getContext().toString());
       auditService.setHostName(findHostName());
       auditService.setLogging(Boolean.parseBoolean(getFrameworkLogging()));
       
    } 

    private String findHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}

    /*
	@BeforeClass(alwaysRun=true)
	@Parameters( "nativeApplicationId" )
	public void setNativeApplication(@Optional("") String nativeApplicationId) {
		browserStackAppManager
			.prepare(nativeApplicationId).ifPresent(app -> {
				appiumHash = app.getHashedId();
				deviceID = app.getPlatform();
			});
	}
	*/
	
    protected void setDriver(AllegisDriver driverSession) {
    	sessionId.set(driverSession.getSessionId());
    	threadedDriver.set(driverSession);
    }
    
    public AllegisDriver getDriver() {
    	return threadedDriver.get();
    }
    
    private java.util.Optional<AllegisDriver> getAllegisDriver() {
    	return java.util.Optional.ofNullable(threadedDriver.get());
    }

    protected AllegisDriver testStart(String testName) {
		setTestName(testName);

		String pageUrl = getContext().getPageUrl();
		String url     = pageUrl.isEmpty() ? appURLRepository.getString((getApplicationUnderTest() + "_" + getTestEnvironment()).toUpperCase()) : pageUrl;
		
		getDriver().get(url);

		return getDriver();
	}
   
    protected void quitDriver() {
        try {
	        getAllegisDriver().filter(driver -> !driver.toString().contains("null")).ifPresent(AllegisDriver::quit);
        } catch (Exception e) {
			logger.error("Error while quiting WebDriver: ", e);
		} finally {
			threadedDriver.remove();			
		}
    }

    @BeforeMethod(alwaysRun = true)
    public synchronized void driverSetup(ITestContext context) throws Exception {
    	DriverManager driverManager = new DriverManager(getContext());

    	driverManager.quit(threadedDriver.get());
    	
		String location   = getRunLocation();
		
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
		
		if (!RunLocations.isValid(location))
			throw new AutomationException("invalid run location = " + location);
				
		if (location.equalsIgnoreCase(LOCAL)) 
			setDriver(driverManager.setupLocalDriver());

		if (location.equalsIgnoreCase(DOCKER)) 	
			setDriver(driverManager.setupDockerGridDriver(getRemoteURL()));

		if (location.equalsIgnoreCase(BROWSERSTACK) && deviceID.isEmpty()) 					//WEB Browserstack 
			setDriver(driverManager.setupBrowserStackDriver(getRemoteURL()));

		if (location.equalsIgnoreCase(BROWSERSTACK) && !deviceID.isEmpty()) 				//NATIVE MOBILE Browserstack 
			setDriver(driverManager.setupRemoteAppiumDriver(getDeviceName(), getRemoteURL()));
		
		if (location.equalsIgnoreCase(SIMULATOR)) 
			setDriver(driverManager.setupLocalAppiumDriver(getDeviceName(), getNativeApp(), getServiceUrl()));
		
		if (!location.equalsIgnoreCase(API))
			auditService.setBrowserInfo(getDriver().getBrowserInfo());
    }
    
    @AfterMethod(alwaysRun = true)
    public void driverTeardown(ITestResult testResult) {
    	if(getDriver() == null ) 
    		return;
    	
    	double executionTime = ((double)testResult.getEndMillis() - (double)testResult.getStartMillis())/1000;
    	
    	if (testResult.isSuccess()) 
    		AuditLogger.log(executionTime);
    	else {
    		TestInfo testInfo = new TestInfo(testResult);
    		
    		String exceptionMessage = testInfo.getException();
    		String stackTrace       = testInfo.getStackTrace();

    		AuditLogger.log(exceptionMessage, stackTrace, executionTime);
    		
    		takeScreenShotOnError(testResult);
    	}
    	
   		reportToBrowserStack(testResult);
   		quitDriver();
    }
    
    private URL getRemoteURL() throws MalformedURLException {
    	
    	String gridUrl = getContext().getGridUrl();
    	
    	return gridUrl.isEmpty() ? remoteUrlProvider.getURL(getRunLocation()) 
    			 				 : new URL(gridUrl);
    	
    }

	public void reportToBrowserStack(ITestResult result) {
		if (!getRunLocation().equalsIgnoreCase(BROWSERSTACK) || result.getStatus() != ITestResult.FAILURE ) 
			return;
		
		String message = of(result.getThrowable()).map(Throwable::getMessage).orElse("Check the application log for more details.");
		
		browserStackApi.markFail(sessionId.get(), isAppAutomate(), message );
	}

	private boolean isAppAutomate() {
		return isNotEmpty(getAppiumApplicationHash());
	}
    
    private void takeScreenShotOnError(ITestResult testResult) {
        Reporter.setCurrentTestResult(testResult);
        
        WebDriver drv = getDriver().getWebDriver();
        
        TestReporter.logScreenshot(testResult.getInstanceName().replace(".", Constants.DIR_SEPARATOR), drv);

        failedScreenshot(drv); 
        
        TestReporter.logConsoleErrors(getDriver()); 
	}
    
    @Attachment(type = "image/png")
    public static byte[] failedScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            TestReporter.log("Screenshot for Allure was not taken: " + e.getMessage());
        }

        return new byte[0];
    }	
    
    
    //write test method info to json file for GCP
    
    private String testJsonFilePath = "target/testMethodInfoGCP.json";
    private static List<TestMethodInfo> testMethodInfoList = new ArrayList<>();
    	
    
    @AfterMethod(alwaysRun=true)
    public void getTestMethodInfo(ITestResult testResult) throws SecurityException {
    
    	TestInfo testInfo = new TestInfo(testResult);
    	
        testMethodInfoList.add(
        						new TestMethodInfo(testInfo.getPackageName(),
        										   testInfo.getTestClassName(),
        										   testInfo.getMethodName(), 
        										   testInfo.getTestGroups(), 
        										   testInfo.getExecutionTime(),
        										   getContext().getEnvironment(),
        										   testInfo.getStatus(),
        										   testInfo.getV1TestId(),
        										   testInfo.getException()));

    }
    
    private Instant startTime;
        
    @BeforeSuite(alwaysRun=true)
    public void getStartTime() {
    	this.startTime = Instant.now();
    }

    
    @AfterSuite(alwaysRun=true)
    public void saveJsonFileForGCP() throws IOException {
    	
    	Instant endTime = Instant.now();    	
    	Long suiteTime = Duration.between(startTime, endTime).toMillis();
    	
        TestMethodsInfo testMethodsInfo = new TestMethodsInfo(testMethodInfoList);
	 
        testMethodsInfo.calculatePassedTestCount();
        testMethodsInfo.calculateFailedTestCount();        
        testMethodsInfo.calculateSkippedTestCount();   
        
        testMethodsInfo.calculateTotalTestExecutionTime();
        testMethodsInfo.setSuiteExecutionTime(suiteTime);
        
        testMethodsInfo.setUiTCs();
        testMethodsInfo.setAPiTCs();
        File jsonFile       = new File(testJsonFilePath);
	    ObjectMapper mapper = new ObjectMapper();
	    	
	    mapper.writeValue(jsonFile, testMethodsInfo);
	    
	    jsonFile            = new File(testJsonFilePath);
	    assertTrue(jsonFile.length() > 0, "json file is empty!");
    }

    
    class TestMethodsInfo {

    	public List<TestMethodInfo> testMethods;
    	
    	public long passedCount;
    	public long failedCount;
    	public long skippedCount;
    	
    	public long totalExecutionTime;
    	public long suiteExecutionTime;

    	public long uiTCsCount;
    	public long aPiTCsCount;
	 
	    public TestMethodsInfo(List<TestMethodInfo> testMethods) {
	        this.testMethods = testMethods;
	    }
	    
	    public void calculatePassedTestCount() {
	    	this.passedCount = calculateTestCount("success");
	    }
	    
	    public void calculateFailedTestCount() {
	    	this.failedCount = calculateTestCount("failure");
	    }

	    public void calculateSkippedTestCount() {
	    	this.skippedCount = calculateTestCount("skip");
	    }
	    
	    public long calculateTestCount(String status) {
	    	return this.testMethods.stream()
	    					       .filter(t -> t.status.equalsIgnoreCase(status))
	    						   .count();
	    }

	    public void calculateTotalTestExecutionTime() {
	    	this.totalExecutionTime = this.testMethods.stream()
					    							  .mapToLong(t -> t.executionTime)
					    							  .sum();
	    }
	    
	    public void setSuiteExecutionTime(long time) {
	    	this.suiteExecutionTime = time;
	    }
	    

	    public void setUiTCs() {
			this.uiTCsCount = (int) tCs_lst
								.stream()
								.filter(s -> s.contains(LOCAL))
								.count()
								+ (int) tCs_lst
								.stream()
								.filter(s -> s.contains(DOCKER))
								.count()
								+ (int) tCs_lst
								.stream()
								.filter(s -> s.contains(BROWSERSTACK))
								.count();
		}

		public void setAPiTCs() {
			this.aPiTCsCount = (int) tCs_lst
								.stream()
								.filter(s -> s.contains(API))
								.count();
		}
	    
	}
    
    class TestMethodInfo {
    	
    	public String packageName;
    	public String testClassName;
    	public String testMethodName;
    	
    	public String groups;
    	public long executionTime;
    	public String environment;
    	public String status;
    	public String v1TestId;
    	public String exception;
    	
    	public TestMethodInfo(String packageName, 
    						  String testClassName,
    						  String testMethodName,
    						  String groups, 
    						  long executionTime, 
    						  String environment, 
    						  String status, 
    						  String v1TestId, 
    						  String exception) {
    		this.packageName    = packageName;
    		this.testClassName  = testClassName;
    		this.testMethodName = testMethodName;
    		this.groups         = groups;
    		this.executionTime  = executionTime;
    		this.environment    = environment;
    		this.status         = status;
    		this.v1TestId       = v1TestId;
    		this.exception      = exception;
    	}
    	
    }
    
    @AfterMethod(alwaysRun = true)
	public void getTcCount(ITestResult testResult) {
		if (testResult.isSuccess()) {
			tCs_lst.add(getRunLocation().toLowerCase());
		} else {
		}
	}
    
}