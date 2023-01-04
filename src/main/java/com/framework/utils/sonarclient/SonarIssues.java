package com.framework.utils.sonarclient;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SonarIssues {

	private List<Issue> issues;
	
	public SonarIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	public List<Issue> getIssues() {
				
		return issues;
	
	}
	
	public List<Issue> getInfoIssues(String project) {
		return filterBy(this.issues, filteringPredicateBy("INFO", project, "OPEN"));
	}
	
	public List<Issue> getMinorIssues(String project) {
		return filterBy(this.issues, filteringPredicateBy("MINOR", project, "OPEN"));
	}

	public List<Issue> getMajorIssues(String project) {
		return filterBy(this.issues, filteringPredicateBy("MAJOR", project, "OPEN"));
	}

	public List<Issue> getCriticalIssues(String project) {
		return filterBy(this.issues, filteringPredicateBy("CRITICAL", project, "OPEN"));
	}

	public List<Issue> getBlockerIssues(String project) {
		return filterBy(this.issues, filteringPredicateBy("BLOCKER", project, "OPEN"));
	}
	
	private List<Issue> filterBy(List<Issue> issues, Predicate<Issue> predicate) {
		return issues.stream()
					 .filter(predicate)
					 .collect(Collectors.toList());
	}

	private Predicate<Issue> filteringPredicateBy(String severity, String project, String status) {
			
		return s -> s.getSeverity().toUpperCase().equalsIgnoreCase(severity) && 
				    s.getProject().equalsIgnoreCase(project)                 &&  
				    s.getStatus().toUpperCase().contains(status);

	}
		
}
