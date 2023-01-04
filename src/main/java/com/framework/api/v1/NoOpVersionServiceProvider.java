package com.framework.api.v1;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoOpVersionServiceProvider implements VersionOneService {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	public NoOpVersionServiceProvider() {
		logger.warn("Created dummy Version One Service Provider.");
	}

	@Override
	public Optional<String> getOriginalTestAssetId(String versionOneTestId) {
		return "RT-INVALID".equals(versionOneTestId) ? Optional.empty() : Optional.ofNullable(versionOneTestId);
	}

}
