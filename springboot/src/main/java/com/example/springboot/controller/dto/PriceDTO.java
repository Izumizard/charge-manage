package com.example.springboot.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;

/**
 * 功能：
 * 日期：2024/1/23 23:16
 */
@Data
public class PriceDTO {
    private Integer start;
    private Integer end;
    private String start_time;
    private String end_time;
    private BigDecimal price;

    public void setStart_time(Time start_time) {
        this.start_time = start_time.toString();
        this.start = start_time.toLocalTime().getHour();
    }

    public void setEnd_time(Time end_time) {
        LocalTime localTime = end_time.toLocalTime();
        if (localTime.equals(LocalTime.of(23, 59, 59))) {
            this.end = 24;
        } else {
            this.end_time = end_time.toString();
            this.end = localTime.getHour();
        }
    }

}

