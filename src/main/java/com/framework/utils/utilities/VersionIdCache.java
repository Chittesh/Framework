package com.framework.utils.utilities;

import java.util.Optional;

public interface VersionIdCache {

	void put(String id, String versionOneId );
	
	Optional<String> getVersionOneId(String id);
	
}
