package com.example.springboot.controller.DTO;

import com.example.springboot.common.StatusType;
import com.example.springboot.entity.RechargeRecords;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：
 * 日期：2023/11/18 15:50
 */
@Data
public class RechargeRecordsDTO{
    private Integer id;
    private String user_username;
    private String user_avatar;
    private BigDecimal recharge;
    private String number;
    private String type;
    private StatusType status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp recharge_time;

    public RechargeRecordsDTO(RechargeRecords record) {
        this.id = record.getId();
        this.user_username = record.getUser_username();
        this.user_avatar = getUser_avatar();
        this.recharge = record.getRecharge();
        this.number = record.getNumber();
        this.type = record.getType();
        this.status = record.getStatus();
        this.recharge_time = record.getRecharge_time();
    }
    public String getUser_avatar() {
        return user_avatar;
    }
}
