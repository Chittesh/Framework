package com.framework.utils.listeners;

public class TestExecutionTotals {
	
	private int totalNumOfTests;	
	private int totalNumOfPasses;
	private int totalNumOfFailures;
	private int totalNumOfSkips;
	private float totalRunTime;

	public void setTotalNumOfTests(int totalNumOfTests) {
		this.totalNumOfTests = totalNumOfTests;
	}

	public void setTotalNumOfPasses(int totalNumOfPasses) {
		this.totalNumOfPasses = totalNumOfPasses;
	}

	public void setTotalNumOfFailures(int totalNumOfFailures) {
		this.totalNumOfFailures = totalNumOfFailures;
	}

	public void setTotalNumOfSkips(int totalNumOfSkips) {
		this.totalNumOfSkips = totalNumOfSkips;
	}

	public void setTotalRunTime(float totalRunTime) {
		this.totalRunTime = totalRunTime;
	}

	public int getTotalNumOfTests() {
		return totalNumOfTests;
	}

	public int getTotalNumOfPasses() {
		return totalNumOfPasses;
	}

	public int getTotalNumOfFailures() {
		return totalNumOfFailures;
	}

	public int getTotalNumOfSkips() {
		return totalNumOfSkips;
	}

	public float getTotalRunTime() {
		return totalRunTime;
	}

}
