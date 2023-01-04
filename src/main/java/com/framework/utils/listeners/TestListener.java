package com.framework.utils.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.IAnnotationTransformer;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.IResultListener2;
import org.testng.xml.XmlSuite;

import com.allegis.Application;
import com.framework.config.PropertiesConfig;
import com.framework.utils.TestReporter;
import com.framework.utils.annotationsAllegis.RetryAnalyzer;
import com.framework.utils.utilities.AnnotationInspector;

public class TestListener extends TestListenerAdapter implements ISuiteListener, IReporter, IInvokedMethodListener, IAnnotationTransformer, IMethodInterceptor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired(required = false)
	private Set<IReporter> reporters = new HashSet<>();
	
	@Autowired(required = false)
	private Set<IInvokedMethodListener> invokeListeners = new HashSet<>();
	
	@Autowired(required = false)
	private Set<ISuiteListener> suiteListeners = new HashSet<>();
	
	@Autowired(required = false)
	private Set<IResultListener2> resultListeners = new HashSet<>();
	
    @Inject
    private AnnotationInspector annotation;
	
	private static final ConfigurableApplicationContext CONTEXT;

	static {
		CONTEXT = SpringApplication.run(new Class[] {PropertiesConfig.class, Application.class}, new String[] {});
		CONTEXT.registerShutdownHook();
	}
    
    public TestListener() {
    	inject(this);
    }

    /**
     * This method is invoked at the start of each Test Method.
     *
     * @param testContext
     *            test context - testNG handling
     */
    @Override
    public void onTestStart(ITestResult testResult) {
        TestReporter.logToConsoleAndTestNgReport("\n\n TEST STARTED: " + testResult.getInstanceName() + " - " + testResult.getMethod().getMethodName() + "\n\n");
        
        resultListeners.stream()
        	.forEach(listener -> listener.onTestStart(testResult));
    }

    /**
     * This method is invoked at the end of each Test Method. In particular it prints the lists of success test cases, failing ones and skipped ones.
     * Data about the test case outputs are collected in 'onTestSuccess' and 'onTestFailure' methods.
     *
     * @param testContext
     *            test context - testNG handling
     */
    @Override
    public void onFinish(ITestContext testContext) {
        resultListeners.stream()
    		.forEach(listener -> listener.onFinish(testContext));
    }

    /**
     * Functionality to be executed after a test method executes. Ex: Screenshots, VersionOne updates, etc
     * Added code for scenario of duplicate version one test assets in a test class,
     * So that if any of the methods failed, will mark the asset as failure, regardless if any of them have passed.
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    	invokeListeners.stream()
    		.forEach(listener -> listener.afterInvocation(method, testResult, context));
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
    	TestReporter.logToConsoleAndTestNgReport("TEST SKIPPED: " + testResult.getMethod().getMethodName() + "\n\n");
    	
        resultListeners.stream()
    		.forEach(listener -> listener.onTestSkipped(testResult));
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
    	TestReporter.logToConsoleAndTestNgReport("TEST PASSED: " + testResult.getMethod().getMethodName() + "\n\n");
    	 
        resultListeners.stream()
    		.forEach(listener -> listener.onTestSuccess(testResult));
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
    	TestReporter.logToConsoleAndTestNgReport("TEST FAILED: " + testResult.getMethod().getMethodName() + "\n\n");
    	 
    	resultListeners.stream()
			.forEach(listener -> listener.onTestSuccess(testResult));
    }

    /**
     * Generate a VersionOne TestRun object graphical report for the given suites.
     * It will be invoked after all the suite have run and the parameters give all the test results that happened during that run.
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    	reporters.stream()
    		.forEach(reporter -> reporter.generateReport(xmlSuites, suites, outputDirectory));
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    	invokeListeners.stream()
    		.forEach(listener -> listener.beforeInvocation(method, testResult));
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    	invokeListeners.stream()
			.forEach(listener -> listener.afterInvocation(method, testResult));
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    	invokeListeners.stream()
			.forEach(listener -> listener.beforeInvocation(method, testResult, context));
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        return methods;
    }


    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

	@Override
	public void onStart(ISuite suite) {
		
		suiteListeners.stream()
			.forEach(listener -> startListener(listener, suite));
		
	}

	@Override
	public void onFinish(ISuite suite) {
		suiteListeners.stream()
			.forEach(listener -> finnishListener(listener, suite));
	}
	
	private void finnishListener(ISuiteListener listener, ISuite suite) {
		try {
			listener.onFinish(suite);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private void startListener(ISuiteListener listener, ISuite suite) {
		try {
			listener.onStart(suite);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void inject(Object bean) {
		CONTEXT.getAutowireCapableBeanFactory().autowireBean(bean);
	}
	
	public static ConfigurableApplicationContext getContext() {
		return CONTEXT;
	}
}