package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.springboot.common.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：充值记录实体类
 * 日期：2023/11/14 23:54
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("recharge_records")
public class RechargeRecords {
    @TableId(type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    @Alias("用户id")
    private Integer user_id;
    @Alias("用户名")
    private String user_username;
    @Alias("充值金额")
    private BigDecimal recharge;
    @Alias("充值订单号")
    private String number;
    @Alias("支付方式")
    private String type;
    @Alias("充值状态")
    private StatusType status;
    @Alias("充值时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp recharge_time;
    @TableField(exist = false)
    private transient String avatar;

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }



}
