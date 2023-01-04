package com.framework.utils.dataProviders;

import java.util.Comparator;

import org.apache.commons.csv.CSVRecord;

final class CSVRecordComparator implements Comparator<CSVRecord> {
	private final String headerName;

	CSVRecordComparator(String headerName) {
		this.headerName = headerName;
	}

	@Override
	public int compare(CSVRecord obj1, CSVRecord obj2) {

		Integer x = Integer.parseInt(obj1.get(headerName));
		Integer y = Integer.parseInt(obj2.get(headerName));

		return x.compareTo(y);
	}
}