package com.framework.utils;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;

import com.framework.core.interfaces.Button;
import com.framework.core.interfaces.Checkbox;
import com.framework.core.interfaces.Element;
import com.framework.core.interfaces.Label;
import com.framework.core.interfaces.Link;
import com.framework.core.interfaces.Listbox;
import com.framework.core.interfaces.RadioGroup;
import com.framework.core.interfaces.Textbox;
import com.framework.core.interfaces.Webtable;
import com.framework.core.interfaces.impl.ButtonImpl;
import com.framework.core.interfaces.impl.CheckboxImpl;
import com.framework.core.interfaces.impl.ElementImpl;
import com.framework.core.interfaces.impl.LabelImpl;
import com.framework.core.interfaces.impl.LinkImpl;
import com.framework.core.interfaces.impl.ListboxImpl;
import com.framework.core.interfaces.impl.RadioGroupImpl;
import com.framework.core.interfaces.impl.TextboxImpl;
import com.framework.core.interfaces.impl.WebtableImpl;
import com.framework.exception.AutomationException;
import com.framework.utils.utilities.StackTraceInfo;

import io.appium.java_client.AppiumDriver;

public class AllegisDriver implements WebDriver, TakesScreenshot, Interactive, JavascriptExecutor {

	private WebDriver driver;
	
    private int currentPageTimeout    = Constants.PAGE_TIMEOUT;
    private int currentElementTimeout = Constants.ELEMENT_TIMEOUT;
    private int currentScriptTimeout  = Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT;
    
    public AllegisDriver(ChromeOptions options) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	System.setProperty("webdriver.chrome.silentOutput", "true");
    	
    	driver = new ChromeDriver(options);
    }
    
    public AllegisDriver(FirefoxOptions options) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

    	driver = new FirefoxDriver(options);
    }
    
    public AllegisDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public AllegisDriver(DesiredCapabilities caps, URL url) {
    	this(new RemoteWebDriver(url, caps));
    }
    
    public void resizeBrowser(String browserSize) {
		Window window = getAllegisDriver().manage().window();
		
        if (browserSize.contains(",")) {
        	String[] dimensions = browserSize.replace(" ", "").split(",");
        	
        	if (dimensions.length != 2) 
        		window.maximize();
        	else 
        		window.setSize(new Dimension(toInt(dimensions[0]), toInt(dimensions[1])));
        }
        else
       		window.maximize();
	}
    
    private int toInt(String text) {
    	return Integer.parseInt(text);
    }
    
    public String getBrowserInfo() {
    	WebDriver wrappedDriver = getWebDriver();
    	org.openqa.selenium.Capabilities cap = ((RemoteWebDriver) wrappedDriver).getCapabilities();
	    
	    return cap.getBrowserName() + " - " + cap.getBrowserVersion();  
    }
    
    public String getCurrentBrowserName() {
        return getAllegisDriver().getDriverCapability().browserName();
    }

    public String getCurrentBrowserVersion() {
        return getAllegisDriver().getDriverCapability().browserVersion();
    }

    public WebDriver getWebDriver() {
        return driver;
    }
    
    public <T extends WebDriver> Optional<T> getWebDriver(Class<T> driverClass) {
    	return driverClass.isInstance(driver) ? 
    		   Optional.of(driver).map(driverClass::cast) : 
    		   Optional.empty();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public void get(String url) {
    	AuditLogger.collect("( AllegisDriver - get(url) ), ");
    	
        driver.get(url);
    }
    
    public void newTab(String url) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	this.newTab();
    	driver.get(url);
    }
    
    public void newTab() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	if (getCurrentBrowserName().toLowerCase().contains("chrome"))
    		throw new SkipException("newTab() does not work yet for Chrome - use newTabJS() instead");
    	
    	int tabCount = getWindowHandlesCount();
    	
    	this.driver.switchTo().newWindow(WindowType.TAB);

        if (getWindowHandlesCount() != tabCount + 1)
        	throw new AutomationException("new tab is not created!");
    }
    
    public void newTabJS(String url) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	executeScript(format("window.open('%s','_blank');", url));
    }	
            
    public void newWindow(String url) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	this.newWindow();
    	driver.get(url);
    }
    
    public void newWindow() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());

    	int windowCount = getWindowHandlesCount();

    	this.driver.switchTo().newWindow(WindowType.WINDOW);
    	
        if (getWindowHandlesCount() != windowCount + 1)
        	throw new AutomationException("new window is not created!");

    }
    
    public int getWindowHandlesCount() {
    	return driver.getWindowHandles().toArray().length;
    }

    @Override
    public String getCurrentUrl() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.getTitle();
    }
    
    @Deprecated
    public void setScriptTimeout(int timeout) {
        setScriptTimeout(timeout, TimeUnit.SECONDS);
    }

    @Deprecated
    public void setScriptTimeout(int timeout, TimeUnit timeUnit) {
    	if(isAppium()) {
    		return;
    	}
    	
        this.currentScriptTimeout = timeout;
        driver.manage().timeouts().setScriptTimeout(timeout, timeUnit);
    }

    @Deprecated
    public int getScriptTimeout() {
        return currentScriptTimeout;
    }

    @Deprecated
    public void setPageTimeout(int timeout) {
        setPageTimeout(timeout, TimeUnit.SECONDS);
    }

    @Deprecated
    public void setPageTimeout(int timeout, TimeUnit timeUnit) {
        if (!isAppium()) {
            this.currentPageTimeout = timeout;
            driver.manage().timeouts().pageLoadTimeout(timeout, timeUnit);
        }
    }

	private boolean isAppium() {
		return driver instanceof AppiumDriver;
	}

	@Deprecated
    public int getPageTimeout() {
        return currentPageTimeout;
    }

	@Deprecated
    public void setElementTimeout(int timeout) {
        setElementTimeout(timeout, TimeUnit.SECONDS);
    }

	@Deprecated
    public void setElementTimeout(int timeout, TimeUnit timeUnit) {
        this.currentElementTimeout = timeout;
        driver.manage().timeouts().implicitlyWait(timeout, timeUnit);
    }

	@Deprecated
    public int getElementTimeout() {
        return currentElementTimeout;
    }

    public void setFileDetector() {
        ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
    }

    public List<Element> locateElements(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.findElements(by).stream()
        		.map(element -> new ElementImpl(this, by, element))
        		.collect(toList());
    }
    
    @Override
    public List<WebElement> findElements(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.findElements(by);
    }
    
    public Element locateElement(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ElementImpl(this, by);
    }
    
    @Override
    public WebElement findElement(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.findElement(by);
    }

    public Textbox findTextbox(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new TextboxImpl(this, by);
    }

    public List<Textbox> findTextboxes(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return locateElements(by).stream()
        			.map(element -> new TextboxImpl(this, by, element))
        			.collect(toList());
    }

    public Button findButton(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ButtonImpl(this, by);
    }

    public List<Button> findButtons(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return locateElements(by).stream()
        		.map(element -> new ButtonImpl(this, by, element))
        		.collect(toList());
    }

    public Checkbox findCheckbox(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new CheckboxImpl(this, by);
    }

    public List<Checkbox> findCheckboxes(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return locateElements(by).stream()
        		.map(element -> new CheckboxImpl(this, by, element))
        		.collect(toList());
    }

    public Label findLabel(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new LabelImpl(this, by);
    }

    public List<Label> findLabels(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return locateElements(by).stream()
        		.map(element -> new LabelImpl(this, by, element))
        		.collect(toList());
    }

    public Link findLink(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new LinkImpl(this, by);
    }

    public List<Link> findLinks(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return locateElements(by).stream()
        		.map(element -> new LinkImpl(this, by, element))
        		.collect(toList());
    }

    public Listbox findListbox(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ListboxImpl(this, by);
    }

    public RadioGroup findRadioGroup(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new RadioGroupImpl(this, by);
    }

    public Webtable findWebtable(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new WebtableImpl(this, by);
    }

    @Override
    public String getPageSource() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.getPageSource();
    }

    @Override
    public void close() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        driver.close();
    }

    @Override
    public void quit() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.navigate();
    }

    @Override
    public Options manage() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return driver.manage();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Deprecated
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
   
    @Override
    public int hashCode() {
        return super.hashCode();
    }
   
    @Override
    public String toString() {
        return super.toString();
    }

    @Deprecated(forRemoval = true, since = "1.1.4")
    public Object executeJavaScript(String script, Object... parameters) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return executeScript(script, parameters);
    }
    
	@Override
	public Object executeScript(String script, Object... parameters) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return getJavascriptExecutor().executeScript(script, parameters);
	}

	@Override
	public Object executeAsyncScript(String script, Object... parameters) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return getJavascriptExecutor().executeAsyncScript(script, parameters);
	}

    public String getSessionId() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (JavaUtilities.isValid(getRemoteWebDriver().getSessionId())) {
            return getRemoteWebDriver().getSessionId().toString();
        } else {
            return null;
        }
    }

    public Capabilities getDriverCapability() {
        return new Capabilities();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    public class Capabilities {

        public String browserName() {
        	AuditLogger.collect(StackTraceInfo.getMethodInfo());
        	
            return getRemoteWebDriver().getCapabilities().getBrowserName();
        }

        public String browserVersion() {
        	AuditLogger.collect(StackTraceInfo.getMethodInfo());
        	
            return getRemoteWebDriver().getCapabilities().getBrowserVersion();
        }

    }

    @Deprecated
    public Page page() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new Page();
    }

    private AllegisDriver getAllegisDriver() {
        return this;
    }

    private RemoteWebDriver getRemoteWebDriver() {
        return ((RemoteWebDriver) driver);
    }

    @Deprecated
    public class Page {

    	@Deprecated
    	public boolean isDomComplete() {
            return PageLoaded.isDomComplete(getAllegisDriver());
        }

    	@Deprecated
    	public boolean isDomComplete(AllegisDriver oDriver) {
            return PageLoaded.isDomComplete(getAllegisDriver());
        }

    	@Deprecated
    	public boolean isDomComplete(AllegisDriver oDriver, int timeout) {
            return PageLoaded.isDomComplete(getAllegisDriver(), timeout);
        }
    }

    public Actions actions() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new Actions(driver);
    }

	@Override
	public void perform(Collection<Sequence> actions) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		getIteractive().perform(actions);
	}

	private Interactive getIteractive() {
		return Optional.of(driver)
				.filter(Interactive.class::isInstance)
				.map(Interactive.class::cast)
				.orElseThrow(() -> new UnsupportedOperationException("Driver wrapped by allegis driver does not implement necessary interactive interface."));
	}
	
	private JavascriptExecutor getJavascriptExecutor() {
		return Optional.of(driver)
				.filter(JavascriptExecutor.class::isInstance)
				.map(JavascriptExecutor.class::cast)
				.orElseThrow(() -> new UnsupportedOperationException("Driver wrapped by allegis driver does not implement necessary JavascriptExecutor interface."));
	}
	
	@Deprecated
    public String getReadyState(int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        AllegisWait wait = new AllegisWait.Builder()
        									.withDriver(this.getWebDriver())
        									.withTimeout(Duration.ofSeconds(timeout))
        									.build();
        
        String state = (String)wait.wrappedWait()
        						.until(ExpectedConditions.jsReturnsValue("return document.readyState"));
        
        return state;
    }

	@Override
	public void resetInputState() {
		// TODO Auto-generated method stub
		
	}

}