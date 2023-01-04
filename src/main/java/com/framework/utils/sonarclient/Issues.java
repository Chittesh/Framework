package com.framework.utils.sonarclient;

public class Issues {
	
	private int total;
	
	private int p;
	
	private int ps;
	
	private Paging paging;
	
	private int effortTotal;
	
	private int debtTotal;
	
	private Issue[] issueList;
	
	private Component[] components;
	
	private Object[] facets;
	
	private Organization[] organizations;
	
	public Organization[] getOrganizations() {
		return organizations;
	}
	
	public void setOrganizations(Organization[] organizations) {
		this.organizations = organizations;
	}
	
	public Component[] getComponents() {
		return components;
	}
	
	public void setComponents(Component[] components) {
		this.components = components;
	}
	
	public Object[] getFacets() {
		return facets;
	}
	
	public void setFacets(Object[] facets) {
		this.facets = facets;
	}
	
	public Issue[] getIssues() {
		return issueList;
	}
	
	public void setIssues(Issue[] issues) {
		this.issueList = issues;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getP() {
		return p;
	}
	
	public void setP(int p) {
		this.p = p;
	}
	
	public int getPs() {
		return ps;
	}
	
	public void setPs(int ps) {
		this.ps = ps;
	}
	
	public Paging getPaging() {
		return paging;
	}
	
	public void setPaging(Paging paging) {
		this.paging = paging;
	}
	
	public int getEffortTotal() {
		return effortTotal;
	}
	
	public void setEffortTotal(int effortTotal) {
		this.effortTotal = effortTotal;
	}
	
	public int getDebtTotal() {
		return debtTotal;
	}
	
	public void setDebtTotal(int debtTotal) {
		this.debtTotal = debtTotal;
	}
	
	@Override
	public String toString() {
		return "Issues [\n total=" + total + ",\n p=" + p + ", \n ps=" + ps + ", \n paging=" + paging + ", \n effortTotal="
				+ effortTotal + ", \n debtTotal=" + debtTotal + ", \n issue count=" + issueList.length
				+ ", \n component count=" + components.length + ", \n facet count=" + facets.length + "\n]\n\n";
	}
	
}
