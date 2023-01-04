package com.framework.utils.status;

import java.util.Arrays;
import java.util.Optional;

import org.testng.ITestResult;

public enum TestStatus {

	SUCCESS(ITestResult.SUCCESS, "Passed", 5771),
	FAILURE(ITestResult.FAILURE, "Failed", 5772),
	SKIP(ITestResult.SKIP, "Skipped", 243887),
	STARTED(ITestResult.STARTED, "Started", null);
	
	
	private int code;
	private String text;
	private Integer vOneCode;

	private TestStatus(int code, String text, Integer vOneCode) {
		this.code = code;
		this.text = text;
		this.vOneCode = vOneCode;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * Get the status code by its  code or throws an exception if it cannot be found.
	 * @param code The code to search the status for.
	 * @return The status to be returned.
	 */
	public static TestStatus byCode(int code) {
		return findByCode(code)
			.orElseThrow(() -> new IllegalStateException(String.format("Could not map unknown code [%s] to any status.", code)));
	}
	
	/**
	 * Search for the status giving by its code and falls back to a default value if it cannot be found.
	 * @param code The code for which the status is wanted.
	 * @param missing The value to be used if the code cannot be found.
	 * @return The status found or the given default.
	 */
	public static TestStatus byCodeWithDefault(int code, TestStatus missing) {
		return findByCode(code)
			.orElse(missing);
	}

	public static Optional<TestStatus> findByCode(int code) {
		return Arrays.stream(values())
			.filter(status -> status.getCode() == code)
			.findAny();
	}
	
	public static TestStatus from(ITestResult result) {
		return byCodeWithDefault(result.getStatus(), FAILURE);
	}

	public int getCode() {
		return code;
	}

	public Integer getVOneCode() {
		return vOneCode;
	}
}
