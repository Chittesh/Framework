package com.framework.api.v1.data;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoOpResource implements TestResource {

	private Logger logger = LogManager.getLogger(this.getClass());

	
	private String testId;

	public NoOpResource(String testId ) {
		this.testId = testId;
	}

	@Override
	public Optional<String> getOriginalTestAssetId() {
		logger.debug("NoOpResource attempt to locate test {}", testId);
		return Optional.empty();
	}
}
