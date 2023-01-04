package com.framework.utils.pagespeedapi;

import java.util.Collection;
import java.util.Optional;

import com.google.api.services.pagespeedonline.v5.model.LighthouseAuditResultV5;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;

public class PageSpeedResponse {
	
	private PagespeedApiPagespeedResponseV5 response;
	
	public PageSpeedResponse(PagespeedApiPagespeedResponseV5 response) {
		this.response = response;
	}
	
	public PagespeedApiPagespeedResponseV5 getResponse() {
		return this.response;
	}
	
	public Double getPerformanceScore() {
		return Double.parseDouble(getPerformanceScore(this.response));
	}
		
	public Double getSpeedIndex() {
	    return getMetric("speed-index");
	}
		
	public Double getTotalBlockingTime() {
	    return getMetric("total-blocking-time");
	}
		
	public Double getTimeToInteractive() {
	    return getMetric("interactive");
	}
		
	public Double getFirstContentfulPaint() {
	    return getMetric("first-contentful-paint");
	}
		
	public Double getLargestContentfulPaint() {
	    return getMetric("largest-contentful-paint");
	}
	
	public String getPerformanceScore(PagespeedApiPagespeedResponseV5 pageSpeedResponse) {

		return pageSpeedResponse.getLighthouseResult()
				.getCategories()
				.getPerformance()
				.getScore()
				.toString();

	}
		
	public Double getMetric(String metricName) {

		Optional<LighthouseAuditResultV5> metric = getMetrics().stream().filter(v -> v.getId().equalsIgnoreCase(metricName)).findFirst(); 

		if (metric.isEmpty())
			throw new IllegalStateException(metricName + " metric was not found!");

		Double value = metric.get().getNumericValue();

		return (double) Math.round(value);
	}

	public Collection<LighthouseAuditResultV5> getMetrics() {

		return this.response.getLighthouseResult()
				.getAudits()
				.values();								

	}

}
