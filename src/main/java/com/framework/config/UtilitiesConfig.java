package com.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.framework.utils.utilities.ParameterRtNumberExtractor;
import com.framework.utils.utilities.PreambleRtNumberExtractor;
import com.framework.utils.utilities.RtNumberExtractor;

@Configuration
public class UtilitiesConfig {
	
	@Value("${USE_RT_FROM_PARAMETERS:false}")
	private boolean useRtFromParameter;
	
	@Bean
	public RtNumberExtractor rtNumberExtractor() {
		return useRtFromParameter ? new ParameterRtNumberExtractor() : new PreambleRtNumberExtractor();
	}

}
