package com.framework.utils.performance;

import org.apache.commons.lang.time.StopWatch;

import com.framework.utils.AllegisDriver;
import com.framework.utils.Constants;
import com.framework.utils.TestReporter;
import com.framework.utils.WaitAllegis;

public class Transactions {
    String transactionName = null;
    private StopWatch trans = null;
    AllegisDriver driver = null;

    /**
     * Constructor
     *
     * @param transactionName
     *            Every transactionName should be unique trans name
     */
    public Transactions(AllegisDriver driver, String transactionName) {
        this.trans = new StopWatch();
        this.driver = driver;
        this.transactionName = transactionName;
    }

    /**
     * Starts the transaction counter
     *
     * @param none
     *
     * @return none
     */
    public void startTransaction() {
        TestReporter.logTransaction("Starting transaction for '" + transactionName + "'");
        trans.start();
    }

    /**
     * Stops the transaction counter
     *
     * @param none
     *
     * @return none
     */
    public void stopTransaction() {
        WaitAllegis.syncJS(driver, Constants.PAGE_TIMEOUT);
        TestReporter.logTransaction("Stopping transaction for '" + transactionName + "'");
        trans.stop();
    }

    /**
     * Returns the transaction time in Long
     *
     * @param none
     *
     * @return Long
     */
    public Long getTransaction() {
        return trans.getTime();
    }

    /**
     * Returns the transaction time in Seconds
     *
     * @param none
     *
     * @return Double
     */
    public Double getTransactionInSec() {
        return Double.parseDouble(String.format("%.3f", getTransaction() / 1000.0f));
    }

    /**
     * Stops the transaction counter and returns the transaction time in Long
     *
     * @param none
     *
     * @return Long
     */
    public Long stopAndGetTransaction() {
        stopTransaction();
        return getTransaction();
    }
}