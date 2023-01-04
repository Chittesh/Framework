package com.framework.utils.sonarclient;

public class Issue {
	private String key;
	private String rule;
	private String severity;
	private String component;
	private String project;
	private Object[] flows;
	private String status;
	private String message;
	private String effort;
	private String debt;
	private String author;
	private String[] tags;
	private String creationDate;
	private String updateDate;
	private String type;
	private String scope;
	private Boolean quickFixAvailable;
	private int line;
	private String hash;
	private Object textRange;
	private String resolution;
	private String closeDate;
	private String assignee;
	private String organization;
	
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAssignee() {
		return assignee;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public Object getTextRange() {
		return textRange;
	}
	public void setTextRange(Object textRange) {
		this.textRange = textRange;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public Object[] getFlows() {
		return flows;
	}
	public void setFlows(Object[] flows) {
		this.flows = flows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEffort() {
		return effort;
	}
	public void setEffort(String effort) {
		this.effort = effort;
	}
	public String getDebt() {
		return debt;
	}
	public void setDebt(String debt) {
		this.debt = debt;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Boolean getQuickFixAvailable() {
		return quickFixAvailable;
	}
	public void setQuickFixAvailable(Boolean quickFixAvailable) {
		this.quickFixAvailable = quickFixAvailable;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	
}
