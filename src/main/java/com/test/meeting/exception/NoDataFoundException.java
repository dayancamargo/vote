package com.test.meeting.exception;

public class NoDataFoundException extends BaseException{

    static String TITTLE = "Data cannot be found";

    public NoDataFoundException() {
        super(TITTLE);
    }

    public NoDataFoundException( String detail) {
        super(TITTLE, detail);
    }
}
