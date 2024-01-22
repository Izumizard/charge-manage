package com.example.springboot.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * 功能：
 * 日期：2024/1/24 0:12
 */
@Data
public class PriceRequest {
    private Integer start;
    private Integer end;
    private BigDecimal price;

    public PriceRequest(Integer start, Integer end, BigDecimal price) {
        this.start = start;
        this.end = end;
        this.price = price;
    }
}
