package com.framework.utils.performance;

import org.openqa.selenium.JavascriptExecutor;

import com.framework.utils.AllegisDriver;
import com.framework.utils.Constants;
import com.framework.utils.WaitAllegis;

public class Perf {

    private Perf() {
        throw new IllegalStateException("Performance Utility Class");
    }

    /**
     * @summary - Get the timing related performance information for a given page with the performance timing API
     * @param -
     *            AllegisDriver
     *            getWrappedDriver
     * @return - Long loadTime for the specific page
     *
     *         Performance Timing Events flow
     *         navigationStart -> redirectStart -> redirectEnd -> fetchStart -> domainLookupStart -> domainLookupEnd
     *         -> connectStart -> connectEnd -> requestStart -> responseStart -> responseEnd
     *         -> domLoading -> domInteractive -> domContentLoaded -> domComplete -> loadEventStart -> loadEventEnd
     */

    public static Long pageLoadTime(AllegisDriver driver) {
        return measurePerfTime(driver, "return performance.timing.loadEventEnd - performance.timing.navigationStart;");
    }

    public static Long navigationStart(AllegisDriver driver) {
        return measurePerfTime(driver, "return window.performance.timing.navigationStart");
    }

    public static Long responseStart(AllegisDriver driver) {
        return measurePerfTime(driver, "return window.performance.timing.responseStart");
    }

    public static Long domComplete(AllegisDriver driver) {
        return measurePerfTime(driver, "return window.performance.timing.domComplete");
    }

    public static Long backendPerf(AllegisDriver driver) {
        return responseStart(driver) - navigationStart(driver);
    }

    public static Long frontendPerf(AllegisDriver driver) {
        return domComplete(driver) - responseStart(driver);
    }

    public static Long overallPerf(AllegisDriver driver) {
        return domComplete(driver) - navigationStart(driver);
    }

    protected static Long measurePerfTime(AllegisDriver driver, String javaScript) {
        Long perfTime = (Long) ((JavascriptExecutor) driver.getWebDriver()).executeScript(javaScript);
        WaitAllegis.syncJS(driver, Constants.ELEMENT_TIMEOUT);
        return perfTime;
    }
}