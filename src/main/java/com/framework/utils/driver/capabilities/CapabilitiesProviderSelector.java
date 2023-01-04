package com.framework.utils.driver.capabilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CapabilitiesProviderSelector {

	private final List<CapabilitiesProvider> capabilityProviders;

	private final EmptyCapabilityProvider emptyCapabilityProvider;

	@Autowired
	public CapabilitiesProviderSelector(List<CapabilitiesProvider> capabilityProviders,
			EmptyCapabilityProvider emptyCapabilityProvider) {
		this.capabilityProviders = capabilityProviders;
		this.emptyCapabilityProvider = emptyCapabilityProvider;
	}

	public CapabilitiesProvider select(String browserUnderTest, String browserVersion, String operatingSystem,
			String mobileName) {

		return capabilityProviders.stream()
				.filter(provider -> provider.canCreate(browserUnderTest, browserVersion, operatingSystem, mobileName))
				.findAny().orElse(emptyCapabilityProvider);

	}
}
