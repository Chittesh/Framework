package com.framework.utils.sonarclient;

public class SonarMetrics {
	
	private String type;
	private String projectId;
	private String branchName;
	
	private int blockerCount;
	private int criticalCount;
	private int majorCount;
	private int minorCount;
	private int infoCount;
	
	private int bugCount;
	private int vulnerabilityCount;
	private int codeSmellCount;

	public SonarMetrics(int blockerCount, 	
						int criticalCount, 
						int majorCount, 
						int minorCount, 
						int infoCount,
						int bugCount, 
						int vulnerabilityCount, 
						int codeSmellCount,
						String projectId) {
		
		this.blockerCount 		= blockerCount;
		this.criticalCount 		= criticalCount;
		this.majorCount 		= majorCount;
		this.minorCount 		= minorCount;
		this.infoCount 			= infoCount;
		
		this.bugCount           = bugCount;
		this.vulnerabilityCount = vulnerabilityCount;
		this.codeSmellCount     = codeSmellCount;
		
		this.projectId          = projectId;
			
		this.type               = "SONAR METRICS";
		
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public int getBlockerCount() {
		return blockerCount;
	}

	public int getCriticalCount() {
		return criticalCount;
	}

	public int getMajorCount() {
		return majorCount;
	}

	public int getMinorCount() {
		return minorCount;
	}

	public int getInfoCount() {
		return infoCount;
	}

	public int getBugCount() {
		return bugCount;
	}

	public int getVulnerabilityCount() {
		return vulnerabilityCount;
	}

	public int getCodeSmellCount() {
		return codeSmellCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
}
