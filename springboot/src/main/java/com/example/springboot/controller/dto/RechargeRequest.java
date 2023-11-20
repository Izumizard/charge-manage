package com.example.springboot.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能：用户id和用户余额
 * 日期：2023/11/13 0:03
 */
@Data
public class RechargeRequest {
    private Integer userId;
    private BigDecimal rechargeAmount;
    private String user_username;

    public String getUser_username() {
        return user_username;
    }

    public Integer getUserId() {
        return userId;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }
}
