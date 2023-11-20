package com.example.springboot.controller.dto;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：
 * 日期：2024/1/10 13:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("orders")
public class OrdersDTO {
    @TableId(type = IdType.AUTO)
    @Alias("序号")
    private Long id;
    @Alias("订单号")
    private String order_no;
    @Alias("充电站名称")
    private String station_name;
    @Alias("用户名")
    private String username;
    @Alias("充电桩编号")
    private String port_number;
    @Alias("预约开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp start_time;
    @Alias("预约结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp end_time;
    @Alias("订单状态")
    private String status;
    @Alias("订单创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp create_time;
    @Alias("订单更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp update_time;
    @Alias("订单价格")
    private BigDecimal price;
}
