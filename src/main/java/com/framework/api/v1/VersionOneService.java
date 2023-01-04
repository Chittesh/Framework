package com.framework.api.v1;

import java.util.Optional;

import org.apache.xmlbeans.XmlException;

public interface VersionOneService {

	Optional<String> getOriginalTestAssetId(String versionOneTestId);
	
}
