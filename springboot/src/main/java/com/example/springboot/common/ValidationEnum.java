package com.example.springboot.common;

public enum ValidationEnum {
    LOGIN(1), FORGET_PWD(2);

    private Integer code;

    public Integer getCode() {
        return code;
    }

    ValidationEnum(Integer code) {
        this.code = code;
    }


}
