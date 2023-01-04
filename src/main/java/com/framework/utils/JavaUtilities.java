package com.framework.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import java.util.Collection;
import java.util.Map;

public class JavaUtilities {
	
	private JavaUtilities() {
		
	}
	
	public static boolean isValid(Object obj) {
		if (obj == null) {
			return false;
		}

		if (
				(obj instanceof String && isEmpty((String) obj)) || 
				(obj instanceof Collection<?> && ((Collection<?>) obj).isEmpty()) || 
				(obj instanceof Map<?, ?> && ((Map<?, ?>) obj).isEmpty())
			)
			return false;

		return true;
	}
}
