package com.test.meeting.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    @Getter
    private final Error error;

    public BaseException(String title) {
        error = new Error(title);
    }

    public BaseException(String title, String detail)  {
        error = new Error(title, detail);
    }
	
}