package com.example.springboot.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：
 * 日期：2023/12/31 3:27
 */
@Data
public class OrdersRequest {
    private Integer user_id;
    private String status;
    private BigDecimal price;
    private Integer port_id;
    private Integer station_id;
    private Timestamp start_time;
    private Integer duration;
}
