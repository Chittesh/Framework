package com.framework.utils;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.allegis.service.phoenix.impl.NewrelicExecutionLog;
import com.allegis.service.phoenix.impl.SimpleExecutionLog;

@Service
public class PageSpeedMetricsLogger {

	@Inject
	private NewrelicExecutionLog newrelicLog;

	@Inject
	private SimpleExecutionLog sqlLog;

	public String log(PageSpeedMetrics metrics, String projectId, String branchName) {

		String status = newrelicLog.log(LocalDateTime.now().toString(), 
										BaseTest.getContext(), 
										metrics);
		
		sqlLog.savePageSpeedMetrics(LocalDateTime.now(), 
				 					projectId,
				 					branchName,
				 					metrics.getPerformanceScore().floatValue(),
			 						metrics.getSpeedIndex().floatValue(),
			 						metrics.getTotalBlockingTime().floatValue(),
			 						metrics.getTimeToInteractive().floatValue(),
			 						metrics.getFirstContentfulPaint().floatValue(),
			 						metrics.getLargestContentfulPaint().floatValue());
			
		return status;
		
	}
	
}
