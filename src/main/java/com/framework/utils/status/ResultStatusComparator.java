package com.framework.utils.status;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.ITestResult;

public class ResultStatusComparator implements Comparator<ITestResult> {

	private List<Integer> stata;

	/**
	 * Create a comparator of result status in the order of the priority, the first one being the higher priority
	 * status to the last one being the least priority status. Use the constants in the {@link ITestResult} for the
	 * status labeling. 
	 * 
	 * Example: 
	 * 		ResultStatusComparator comparator = ResultStatusComparator(ITestResult.SUCCESS, ITestResult.SKIP, ITestResult.FAILURE );
	 * 
	 *  Will create a comparator that consider the success the maximum result and failure the the minimum. 
	 * 
	 * @param status The list of status by priority.
	 */
	public ResultStatusComparator(int ... status ) {
		stata = Arrays.stream(status)
				.mapToObj(Integer::valueOf)
				.collect(Collectors.toList());
	}
	
	@Override
	public int compare(ITestResult o1, ITestResult o2) {
		return stata.indexOf(o2.getStatus()) - stata.indexOf(o1.getStatus());
	}

}
