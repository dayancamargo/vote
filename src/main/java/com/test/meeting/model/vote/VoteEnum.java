package com.test.meeting.model.vote;

public enum VoteEnum {
    SIM("Sim"),
    NAO("Não");

    private String value;

    VoteEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
