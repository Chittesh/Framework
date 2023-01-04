package com.framework.api.v1.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.framework.api.v1.data.acceptanceTest.AcceptanceTest;
import com.framework.api.v1.data.regressionTest.RegressionTest;
import com.framework.api.v1.data.testrun.TestRun;
import com.framework.api.v1.data.testsuite.TestSuite;
import com.framework.utils.Constants;

public class Data {
    private String resource = "/Data";
    
    private Map<String, Function<String, TestResource>> resources = new HashMap<>();
    
    public Data() {
		resources.put(Constants.REGRESSION_PREFIX, this::regressionTest);
		resources.put(Constants.AUTOMATION_PREFIX, this::acceptanceTest);
	}

    public TestResource acceptanceTest(String testId) {
        return new AcceptanceTest(resource, testId);
    }

    public TestResource regressionTest(String testId) {
        return new RegressionTest(resource, testId);
    }
    
    public TestResource noOpTest(String testId) {
    	return new NoOpResource(testId);
    }

    public TestResource createTestResource(String testId) {
    	return resources.entrySet().stream()
    		.filter(entry -> testId.startsWith(entry.getKey()))
    		.findFirst()
    		.map(Entry::getValue)
    		.orElse(this::noOpTest)
    		.apply(testId);
    }
    
    public boolean isValidPrefix(String testId) {
    	return resources.keySet().stream()
    			.anyMatch(testId::startsWith);
    }
    
    public TestSuite testSuite() {
        return new TestSuite(resource);
    }

    public TestRun testRun() {
        return new TestRun(resource);
    }
}
