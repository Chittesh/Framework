package com.framework.core.interfaces.impl.internal;

import com.framework.core.interfaces.Element;

public final class ImplementedByProcessor {
	private ImplementedByProcessor() {
	}

	@SuppressWarnings("rawtypes")
	public static <T> Class getWrapperClass(Class<T> iface) {
		if (iface.isAnnotationPresent(ImplementedBy.class)) {
			ImplementedBy annotation = iface.getAnnotation(ImplementedBy.class);
			Class clazz = annotation.value();
			if (Element.class.isAssignableFrom(clazz)) {
				return annotation.value();
			}
		}
		
		throw new UnsupportedOperationException(
				"Apply @ImplementedBy interface to your Interface if you want to extend ");
	}
}