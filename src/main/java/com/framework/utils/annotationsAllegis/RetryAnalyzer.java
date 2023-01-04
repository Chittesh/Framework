package com.framework.utils.annotationsAllegis;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.framework.utils.listeners.InvalidVersionOnIdException;
import com.framework.utils.utilities.AnnotationInspector;

public class RetryAnalyzer implements IRetryAnalyzer {

    private ThreadLocal<Integer> counter = new ThreadLocal<Integer>() {
    	protected Integer initialValue() {
    		return 0;
    	};
    };
    
    /*
     * Decides how many times a test needs to be rerun and is called every time a test fails.
     *
     * @return
     * - true: if the test method has to be retried
     * - false: if test method has NOT to be retried
     */
    
    private AnnotationInspector annotation = new AnnotationInspector();

    @Override
    public boolean retry(ITestResult result) {
    	if(shouldRetry(result)) {
    		counterIncrement();
    		return true;
    	}
    	        
        reset();
        return false;
    }
    
    private void reset() {
		counter.set(0);
	}

	public boolean shouldRetry(ITestResult result ) {
    	return itFailed(result)
    		&& versionIdIsGood(result)
    		&& retryContBellowRetryLimit(result);
    }

	public boolean itFailed(ITestResult result) {
		return !result.isSuccess();
	}

	public boolean retryContBellowRetryLimit(ITestResult result) {
		return annotation.retryFrom(result)
			.filter(retry -> retry.value() > counter.get() )
			.isPresent();
	}

	private boolean versionIdIsGood(ITestResult result) {
		return !InvalidVersionOnIdException.class.isInstance(result.getThrowable());
	}

	public int getCounter() {
		return counter.get();
	}
	
	public void counterIncrement() {
		counter.set(counter.get() + 1);
	}
}
