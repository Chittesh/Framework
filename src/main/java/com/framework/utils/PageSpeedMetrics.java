package com.framework.utils;

public class PageSpeedMetrics {

	private String type;
	
	private Double performanceScore;
	private Double speedIndex;
	private Double totalBlockingTime;
	private Double timeToInteractive;
	private Double firstContentfulPaint;
	private Double largestContentfulPaint;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Double getPerformanceScore() {
		return performanceScore;
	}
	
	public void setPerformanceScore(Double performanceScore) {
		this.performanceScore = performanceScore;
	}
	
	public Double getSpeedIndex() {
		return speedIndex;
	}
	
	public void setSpeedIndex(Double speedIndex) {
		this.speedIndex = speedIndex;
	}
	
	public Double getTotalBlockingTime() {
		return totalBlockingTime;
	}
	
	public void setTotalBlockingTime(Double totalBlockingTime) {
		this.totalBlockingTime = totalBlockingTime;
	}
	
	public Double getTimeToInteractive() {
		return timeToInteractive;
	}
	
	public void setTimeToInteractive(Double timeToInteractive) {
		this.timeToInteractive = timeToInteractive;
	}
	
	public Double getFirstContentfulPaint() {
		return firstContentfulPaint;
	}
	
	public void setFirstContentfulPaint(Double firstContentfulPaint) {
		this.firstContentfulPaint = firstContentfulPaint;
	}
	
	public Double getLargestContentfulPaint() {
		return largestContentfulPaint;
	}
	
	public void setLargestContentfulPaint(Double largestContentfulPaint) {
		this.largestContentfulPaint = largestContentfulPaint;
	}

}
