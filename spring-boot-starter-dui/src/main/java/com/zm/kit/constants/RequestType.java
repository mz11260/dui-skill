package com.zm.kit.constants;

/**
 * Created by Administrator on 2018/11/23.
 */
public enum RequestType {
    START("start"),
    CONTINUE("continue"),
    END("end");

    private String value;
    RequestType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
