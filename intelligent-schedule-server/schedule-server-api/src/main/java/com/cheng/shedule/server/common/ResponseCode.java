package com.cheng.shedule.server.common;


public enum ResponseCode {
    SUCCESS("200"),
    NOT_FOUND("404"),
    FAIL("500"),
    EXCEPTION("400");

    String code;

    private ResponseCode(String code) {
        this.code = code;
    }
}

