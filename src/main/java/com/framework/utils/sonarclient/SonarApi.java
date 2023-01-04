package com.framework.utils.sonarclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.exception.AutomationException;

import static com.google.common.base.Preconditions.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SonarApi {

	private static final String GET_ISSUES_BY_STATUS_URL              = "/api/issues/search?statuses=%s&componentKeys=%s";
	private static final String GET_ISSUES_BY_STATUS_AND_SEVERITY_URL = GET_ISSUES_BY_STATUS_URL + "&severities=%s";
	private static final String GET_ISSUES_BY_STATUS_AND_TYPE_URL     = GET_ISSUES_BY_STATUS_URL + "&types=%s";
	
	private static final String APPLICATION_JSON                      = "application/json";

	private String hostName;
	private String authorizationCode;
	
	public SonarApi(String hostName, String authorizationCode) {
		this.hostName          = hostName;
		this.authorizationCode = authorizationCode;
	}
	
	public String getHostName() {
		return this.hostName;
	}
	
	public String getAuthorizationCode() {
		return this.authorizationCode;
	}
	
	public List<Issue> getSonarIssuesByStatus(String status, String projectId) {
		
		String getIssuesByStatusUrl = String.format(GET_ISSUES_BY_STATUS_URL, status, projectId);
		
		Issues issues = getSonarIssues(getIssuesByStatusUrl);
		
		return Arrays.asList(issues.getIssues());
		
	}
	
	public int getSonarIssuesByTypeCount(String status, String type, String projectId) {
		
		String getIssuesByTypeUrl = String.format(GET_ISSUES_BY_STATUS_AND_TYPE_URL, status, projectId, type);
		
		Issues issues =  getSonarIssues(getIssuesByTypeUrl);
		
		return issues.getTotal();
	
	}
	
	public int getSonarIssuesBySeverityCount(String status, String severity, String projectId) {
		
		String getIssuesBySeverityUrl  = String.format(GET_ISSUES_BY_STATUS_AND_SEVERITY_URL, status, projectId, severity);
		
		Issues issues = getSonarIssues(getIssuesBySeverityUrl);
		
		return issues.getTotal();
	
	}
	
	private Issues getSonarIssues(String url) {
		
		Response response = executeRequest(url);
			
		checkState(response.isSuccessful(), "request is not successful!");
		checkState(response.code() == 200, "response code is not 200!");
			
		try {
			String responseText = response.body().string();
				
			checkState(!responseText.isEmpty(), "response body is empty!");
				
		    ObjectMapper objectMapper = new ObjectMapper();
			Issues issues = objectMapper.readValue(responseText, Issues.class);

			return issues;
		} catch (IOException e) {
			throw new IllegalStateException("\n\n---- Deserialization failed ----\n\n" + e.getMessage());
		}
	        
	} 

	private Response executeRequest(String url) {
		
		try {
			
			OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
			
			Request request = new Request.Builder().url(this.hostName + url)
										 		   .method("GET", null)
										 		   .addHeader("Accept", APPLICATION_JSON)
										 		   .addHeader("Authorization", this.authorizationCode)
										 		   .build();
			
			Response response = httpClient.newCall(request).execute();
			
			return response;
			
		}
		catch (IOException e) {
			throw new AutomationException("cannot execute request - " + url, e);
		}
	
	} 

	public SonarMetrics getMetrics(String projectId)  {
		
		int infoCount     	   = getSonarIssuesBySeverityCount("OPEN", "INFO"    , projectId);
	    int minorCount    	   = getSonarIssuesBySeverityCount("OPEN", "MINOR"   , projectId);
	    int majorCount    	   = getSonarIssuesBySeverityCount("OPEN", "MAJOR"   , projectId);
	    int criticalCount 	   = getSonarIssuesBySeverityCount("OPEN", "CRITICAL", projectId);
	    int blockerCount  	   = getSonarIssuesBySeverityCount("OPEN", "BLOCKER" , projectId);
	    
	    int bugCount           = getSonarIssuesByTypeCount("OPEN", "BUG"          , projectId);
	    int vulnerabilityCount = getSonarIssuesByTypeCount("OPEN", "VULNERABILITY", projectId);
	    int codeSmellCount     = getSonarIssuesByTypeCount("OPEN", "CODE_SMELL"   , projectId);
	    
		return new SonarMetrics(blockerCount, 
							    criticalCount, 
							    majorCount, 
							    minorCount, 
							    infoCount,
							    bugCount,
							    vulnerabilityCount,
							    codeSmellCount,
							    projectId);
		
	}
		
}
