package com.framework.utils.driver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

public class JsonDeviceDesiredCapabilitiesProducer {
	
	private String resourceName;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	private Logger logger = LogManager.getLogger();

	public JsonDeviceDesiredCapabilitiesProducer(String resourceName) {
		super();
		this.resourceName = resourceName;
	}

	public Optional<DesiredCapabilities> produce(String id) {
		
		try {
			List<Object> devices = objectMapper.readValue(Resources.toString(Resources.getResource(resourceName), StandardCharsets.UTF_8), new TypeReference<List<Object>>(){});
			
			return devices.stream()
					.map(item -> (Map)item)
					.filter(item -> item.get("id").equals(id))
					.map(this::mapDevice)
					.findAny();
			
		} catch (IOException e) {
			logger.error(e);
			return Optional.empty();
		} catch (IllegalArgumentException e) {
			logger.debug("desiredCapabilities.json not present.", e);
			return Optional.empty();
		}
		
	}
	
	private DesiredCapabilities mapDevice(Object device) {
		Map dev = (Map)device;
		dev.remove("id");
		return new DesiredCapabilities(dev);
	}
	
}
