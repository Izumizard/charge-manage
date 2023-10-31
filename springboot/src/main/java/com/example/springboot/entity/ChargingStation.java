package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 功能：充电桩实体类
 * 日期：2023/11/20 23:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("charging_station")
public class ChargingStation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String station_name;
    private String station_address;
    private Double station_longitude;
    private Double station_latitude;
    private String station_status;
    private String open_time;
    private BigDecimal charging_fee;
    private BigDecimal service_fee;
    private String parking_fee;
    @TableField(exist = false)
    private String ports_count;
}
