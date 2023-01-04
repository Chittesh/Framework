package com.framework.utils;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.allegis.service.phoenix.impl.NewrelicExecutionLog;
import com.allegis.service.phoenix.impl.SimpleExecutionLog;
import com.framework.utils.sonarclient.SonarMetrics;

@Service
public class SonarScanMetricsLogger {

	@Inject
	private NewrelicExecutionLog newrelicLog;

	@Inject
	private SimpleExecutionLog sqlLog;
	
	public void log(SonarMetrics metrics) {

		newrelicLog.log(LocalDateTime.now().toString(), 
						BaseTest.getContext(), 
					    metrics);
		
		sqlLog.saveSonarMetrics(LocalDateTime.now(), 
				 				metrics.getProjectId(),
				 				metrics.getBranchName(),
				 				metrics.getBlockerCount(),
				 				metrics.getCriticalCount(),
				 				metrics.getMajorCount(),
				 				metrics.getMinorCount(),
				 				metrics.getInfoCount(),
				 				metrics.getBugCount(),
				 				metrics.getVulnerabilityCount(),
				 				metrics.getCodeSmellCount());
			
	}
	
}
