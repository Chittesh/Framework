package com.framework.api.v1;

import java.util.Optional;

import org.apache.xmlbeans.XmlException;

import com.framework.api.v1.data.Data;

public class ReadOnlyVersionOneServiceProvider implements VersionOneService {

	private Data resourceFactory = new Data();

	@Override
	public Optional<String> getOriginalTestAssetId(String versionOneTestId) {
		try {
			return resourceFactory.createTestResource(versionOneTestId).getOriginalTestAssetId();
		} catch (XmlException e) {
			throw new RuntimeException("cannot get the original test asset id!");
		}
	}

}
