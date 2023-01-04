package com.framework.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;


public class StreamUtils {

	private StreamUtils() {
		// Hide constructor for utils class.
	}
	
	public static <T> Stream<T> streamOf(Iterator<T> iterator) {
		Iterable<T> iterable = () -> iterator;
		return streamOf(iterable);
	}
	
	public static <T> Stream<T> streamOf(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Stream<T> streamOf(T[] array) {
		return (Stream<T>) Arrays.asList(ArrayUtils.nullToEmpty(array))
				.stream();
	}
	
}
