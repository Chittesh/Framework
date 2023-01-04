package com.framework.exception.automation;

import com.framework.exception.AutomationException;

public class MoreThanOneAccountWithSameRowID extends AutomationException {
    private static final long serialVersionUID = 6386227823084919744L;

    public MoreThanOneAccountWithSameRowID() {
    }

    public MoreThanOneAccountWithSameRowID(String message) {
        super(message);
    }

    public MoreThanOneAccountWithSameRowID(String message, Throwable cause) {
        super(message, cause);
    }
}