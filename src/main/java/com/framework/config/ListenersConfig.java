package com.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.framework.utils.listeners.MissingIdSkipListener;
import com.framework.utils.utilities.AnnotationInspector;
import com.framework.utils.utilities.RtNumberExtractor;

@Configuration
public class ListenersConfig {

	@Bean
	@ConditionalOnProperty(name = "VERSIONONE_TESTID_MANDATORY")
	public MissingIdSkipListener idSkipListener(RtNumberExtractor rtExtractor, AnnotationInspector annotation) {
		return new MissingIdSkipListener(rtExtractor, annotation);
	}
}
