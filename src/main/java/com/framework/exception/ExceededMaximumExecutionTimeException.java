package com.framework.exception;

public class ExceededMaximumExecutionTimeException extends RuntimeException {

    private static final String ERROR = "Exceeded Maximum Execution Time Error:";

    private static final long serialVersionUID = -8710980695994382082L;

    public ExceededMaximumExecutionTimeException() {
        super(ERROR);
    }

    public ExceededMaximumExecutionTimeException(String message) {
        super(message);
    }

}

