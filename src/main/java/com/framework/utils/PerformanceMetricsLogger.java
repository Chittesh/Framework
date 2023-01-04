package com.framework.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.openqa.selenium.devtools.v102.performance.model.Metric;
import org.springframework.stereotype.Service;

import com.allegis.service.phoenix.impl.NewrelicExecutionLog;
import com.allegis.service.phoenix.impl.SimpleExecutionLog;

@Service
public class PerformanceMetricsLogger {

	@Inject
	private NewrelicExecutionLog newrelicLog;
	
	@Inject
	private SimpleExecutionLog sqlLog;

	public void log(List<Metric> metrics, String projectId, String branchName, String pageUrl) {

		newrelicLog.log(LocalDateTime.now().toString(), 
						BaseTest.getContext(), 
					    toPerformanceMetrics(metrics, pageUrl));
		
		Map<String, Float> metricsMap = new HashMap<>();
		
		metricsMap.put("timeStamp", 					 getMetric("Timestamp", metrics).floatValue());
		metricsMap.put("audioHandlers", 				 getMetric("AudioHandlers", metrics).floatValue());
		metricsMap.put("documents", 					 getMetric("Documents", metrics).floatValue());
		metricsMap.put("frames", 						 getMetric("Frames", metrics).floatValue());
		metricsMap.put("jsEventListeners",  			 getMetric("JSEventListeners", metrics).floatValue());
		metricsMap.put("layoutObjects", 				 getMetric("LayoutObjects", metrics).floatValue());
		metricsMap.put("mediaKeySessions",  			 getMetric("MediaKeySessions", metrics).floatValue());
		metricsMap.put("mediaKeys", 					 getMetric("MediaKeys", metrics).floatValue());
		metricsMap.put("nodes", 						 getMetric("Nodes", metrics).floatValue());
		metricsMap.put("resources", 					 getMetric("Resources", metrics).floatValue());
		metricsMap.put("contextLifecycleStateObservers", getMetric("ContextLifecycleStateObservers", metrics).floatValue());
		metricsMap.put("v8PerContextDatas", 			 getMetric("V8PerContextDatas", metrics).floatValue());
		metricsMap.put("workerGlobalScopes", 			 getMetric("WorkerGlobalScopes", metrics).floatValue());
		metricsMap.put("uaCssResources", 				 getMetric("UACSSResources", metrics).floatValue());
		metricsMap.put("rtcPeerConnections", 			 getMetric("RTCPeerConnections", metrics).floatValue());
		metricsMap.put("resourceFetchers", 				 getMetric("ResourceFetchers", metrics).floatValue());
		metricsMap.put("adSubFrames", 					 getMetric("AdSubframes", metrics).floatValue());
		metricsMap.put("detachedScriptStates", 			 getMetric("DetachedScriptStates", metrics).floatValue());
		metricsMap.put("arrayBufferContents", 			 getMetric("ArrayBufferContents", metrics).floatValue());
		metricsMap.put("layoutCount", 					 getMetric("LayoutCount", metrics).floatValue());
		metricsMap.put("recalcStyleCount", 				 getMetric("RecalcStyleCount", metrics).floatValue());
		metricsMap.put("layoutDuration", 				 getMetric("LayoutDuration", metrics).floatValue());
		metricsMap.put("recalcStyleDuration", 			 getMetric("RecalcStyleDuration", metrics).floatValue());
		metricsMap.put("devToolsCommandDuration", 		 getMetric("DevToolsCommandDuration", metrics).floatValue());
		metricsMap.put("scriptDuration", 				 getMetric("ScriptDuration", metrics).floatValue());
		metricsMap.put("v8CompileDuration", 			 getMetric("V8CompileDuration", metrics).floatValue());
		metricsMap.put("taskDuration", 					 getMetric("TaskDuration", metrics).floatValue());
		metricsMap.put("taskOtherDuration", 			 getMetric("TaskOtherDuration", metrics).floatValue());
		metricsMap.put("threadTime", 					 getMetric("ThreadTime", metrics).floatValue());
		metricsMap.put("processTime", 					 getMetric("ProcessTime", metrics).floatValue());
		metricsMap.put("jsHeapUsedSize", 				 getMetric("JSHeapUsedSize", metrics).floatValue());
		metricsMap.put("jsHeapTotalSize", 				 getMetric("JSHeapTotalSize", metrics).floatValue());
		metricsMap.put("firstMeaningfulPaint", 			 getMetric("FirstMeaningfulPaint", metrics).floatValue());
		metricsMap.put("domContentLoaded", 				 getMetric("DomContentLoaded", metrics).floatValue());
		metricsMap.put("navigationStart", 				 getMetric("NavigationStart", metrics).floatValue());
		
		sqlLog.savePerformanceMetrics(LocalDateTime.now(), 
									  projectId,
									  branchName,
									  pageUrl, 
									  metricsMap);
			
	}
	
	private PerformanceMetrics toPerformanceMetrics(List<Metric> metrics, String url) {		
		PerformanceMetrics performanceMetrics = new PerformanceMetrics();

		performanceMetrics.setType("Performance Metrics").setUrl(url)
						  .setTimestamp(getMetric("Timestamp", metrics))
						  .setAudioHandlers(getMetric("AudioHandlers", metrics))
						  .setDocuments(getMetric("Documents", metrics))
						  .setFrames(getMetric("Frames", metrics))
						  .setJsEventListeners(getMetric("JSEventListeners", metrics))
						  .setLayoutObjects(getMetric("LayoutObjects", metrics))
						  .setMediaKeySessions(getMetric("MediaKeySessions", metrics))
						  .setMediaKeys(getMetric("MediaKeys", metrics))
						  .setNodes(getMetric("Nodes", metrics))
						  .setResources(getMetric("Resources", metrics))
						  .setContextLifecycleStateObservers(getMetric("ContextLifecycleStateObservers", metrics))
						  .setV8PerContextDatas(getMetric("V8PerContextDatas", metrics))
						  .setWorkerGlobalScopes(getMetric("WorkerGlobalScopes", metrics))
						  .setUaCssResources(getMetric("UACSSResources", metrics))
						  .setRtcPeerConnections(getMetric("RTCPeerConnections", metrics))
						  .setResourceFetchers(getMetric("ResourceFetchers", metrics))
						  .setAdSubframes(getMetric("AdSubframes", metrics))
						  .setDetachedScriptStates(getMetric("DetachedScriptStates", metrics))
						  .setArrayBufferContents(getMetric("ArrayBufferContents", metrics))
						  .setLayoutCount(getMetric("LayoutCount", metrics))
						  .setRecalcStyleCount(getMetric("RecalcStyleCount", metrics))
						  .setLayoutDuration(getMetric("LayoutDuration", metrics))
						  .setRecalcStyleDuration(getMetric("RecalcStyleDuration", metrics))
						  .setDevToolsCommandDuration(getMetric("DevToolsCommandDuration", metrics))
						  .setScriptDuration(getMetric("ScriptDuration", metrics))
						  .setV8CompileDuration(getMetric("V8CompileDuration", metrics))
						  .setTaskDuration(getMetric("TaskDuration", metrics))
						  .setTaskOtherDuration(getMetric("TaskOtherDuration", metrics))
						  .setThreadTime(getMetric("ThreadTime", metrics))
						  .setProcessTime(getMetric("ProcessTime", metrics))
						  .setJsHeapUsedSize(getMetric("JSHeapUsedSize", metrics))
						  .setJsHeapTotalSize(getMetric("JSHeapTotalSize", metrics))
						  .setFirstMeaningfulPaint(getMetric("FirstMeaningfulPaint", metrics))
						  .setDomContentLoaded(getMetric("DomContentLoaded", metrics))
						  .setNavigationStart(getMetric("NavigationStart", metrics));
																  
		return performanceMetrics;
	}
	
	private Number getMetric(String name, List<Metric> metrics) {
		Optional<Metric> metric = metrics.stream().filter(m -> name.equals(m.getName())).findAny();
		
		return metric.isPresent() ? metric.get().getValue() : 0;
	}
	
}
