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
 * 功能：电价实体类
 * 日期：2023/11/20 23:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("electricity_price")
public class ElectricityPrice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer station_id;
    private String start_time;
    private String end_time;
    private String price_type;
    private BigDecimal e_price;
    private BigDecimal service_fee;
    @TableField(exist = false)
    private BigDecimal t_price;
    @TableField(exist = false)
    private String station_name;
}
