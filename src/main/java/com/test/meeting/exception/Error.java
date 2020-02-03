package com.test.meeting.exception;

import com.test.meeting.model.BaseModel;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Error extends BaseModel implements Serializable {

    private final String title;
    private String detail;

    public Error(String detail) {
        this.title = "Unexpected error";
        this.detail = detail;
    }

    public Error(String title, String detail) {
        this.detail = detail;
        this.title = title;
    }
}
