package com.cheng.core.enums;


public enum RegisterType {
    NACOS("nacos");

    String code;

    RegisterType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
