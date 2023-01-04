package com.framework.utils.annotationsAllegis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.framework.utils.Constants;

@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    /**
     * Default failed test Retry count is 0.
     * This value specifies how many times a test needs to be re-executed in case of failures.
     */
    int value() default Constants.DEFAULT_RETRY_COUNT;
}