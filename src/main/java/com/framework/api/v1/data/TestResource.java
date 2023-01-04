package com.framework.api.v1.data;

import java.util.Optional;

import org.apache.xmlbeans.XmlException;

public interface TestResource {
	
	Optional<String> getOriginalTestAssetId() throws XmlException;

}
