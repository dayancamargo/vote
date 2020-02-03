package com.test.meeting.exception;

public class ValidationException extends BaseException{

    static String TITTLE = "Invalid information.";

    public ValidationException() {
        super(TITTLE);
    }

    public ValidationException(String detail) {
        super(TITTLE, detail);
    }
}
