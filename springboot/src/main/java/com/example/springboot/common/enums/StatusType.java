package com.example.springboot.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {
    SUCCESS("充值成功"),
    FAIL("充值失败");
    @EnumValue
    private  String value;

    StatusType(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}

