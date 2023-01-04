package com.framework.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v102.emulation.Emulation;

import org.openqa.selenium.devtools.v102.log.Log;
import org.openqa.selenium.devtools.v102.log.model.LogEntry;

import org.openqa.selenium.devtools.v102.network.Network;
import org.openqa.selenium.devtools.v102.network.model.ConnectionType;
import org.openqa.selenium.devtools.v102.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v102.network.model.ResponseReceived;
import org.openqa.selenium.devtools.v102.performance.Performance;
import org.openqa.selenium.devtools.v102.performance.model.Metric;

import com.framework.exception.AutomationException;
import com.google.common.collect.ImmutableMap;

public class DriverDevTools {
	
	private DevTools devTools;
	
	private List<LogEntry>            logEntryList     = new ArrayList<>();
	private List<JavascriptException> jsExceptionsList = new ArrayList<>();
	private List<RequestWillBeSent>   requestList      = new ArrayList<> ();
	private List<ResponseReceived>    responseList     = new ArrayList<> ();
	
	public DriverDevTools(AllegisDriver driver) {
		if (!(driver.getWebDriver() instanceof ChromeDriver)) 
    		throw new AutomationException("dev tools works only for LOCAL run location and CHROME browser!");

   		this.devTools = ((ChromeDriver) driver.getWebDriver()).getDevTools();
		
		this.devTools.createSession();
		this.enableLogging();
	}
	
	private void enableLogging() {
		enableConsoleInfoLogging();
		enableJavascriptErrorLogging();
		enableNetworkTracking();
		enablePerformanceMetricsLogging();
		enableRequestInterception();
		enableResponseInterception();
	}
	
	private void enablePerformanceMetricsLogging() {
		this.devTools.send(Performance.enable(Optional.empty()));
	}

	public List<Metric> getPerformanceMetricList() {
		return this.devTools.send(Performance.getMetrics());
	}
	
	private void enableConsoleInfoLogging() {
		devTools.send(Log.enable());
		
		devTools.addListener(Log.entryAdded(), 
						     logEntry -> logEntryList.add(logEntry));
	}
	
	public List<LogEntry> getConsoleLogInfoList() {
		return this.logEntryList;
	}
	
	private void enableJavascriptErrorLogging() {
	    this.devTools.getDomains().events()
	    			 .addJavascriptExceptionListener(jsExceptionsList::add);
	}
	
	public List<JavascriptException> getJavascriptExceptionList() {
		return this.jsExceptionsList;
	}
	
	private void enableRequestInterception() {
		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));
		devTools.addListener(Network.requestWillBeSent(), l -> requestList.add(l));
	}
	
	public List<RequestWillBeSent> getRequestList() {
		return this.requestList;
	}
	
	private void enableResponseInterception() {
		devTools.send(new Command<>("Network.enable", ImmutableMap.of()));
		devTools.addListener(Network.responseReceived(), l -> responseList.add(l));
	}
	
	public List<ResponseReceived> getResponseList() {
		return this.responseList;
	}
	
	private void enableNetworkTracking() {
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
	}
	
	public void setNetworkConditions(boolean offline, Number latency, Number downloadThroughput, Number uploadThroughput, ConnectionType connectionType) {
		devTools.send(Network.emulateNetworkConditions(offline, latency, downloadThroughput, uploadThroughput, Optional.of(connectionType)));
	}
	
	public void setGeoLocation(Number latitude, Number longitude, Number accuracy) {
		 devTools.send(Emulation.setGeolocationOverride(Optional.of(latitude), Optional.of(longitude), Optional.of(accuracy)));
	}
	
}
