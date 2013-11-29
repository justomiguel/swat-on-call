package com.swacorp.oncallhelper.exceptions;

/**
 * Created by developer on 11/28/13.
 */
public class SWATOnCallRuntimeException extends RuntimeException {

    private String localMessage;

    public SWATOnCallRuntimeException(String detailMessage, String localMessage) {
        super(detailMessage);
        this.localMessage = localMessage;
    }

    public SWATOnCallRuntimeException(Throwable throwable, String localMessage) {
        super(throwable);
        this.localMessage = localMessage;
    }

    public String getLocalMessage() {
        return localMessage;
    }
}
