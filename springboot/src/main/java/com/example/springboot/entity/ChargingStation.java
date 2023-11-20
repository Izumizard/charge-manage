package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.springboot.controller.dto.CollectionsDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    @TableField(exist = false)
    private BigDecimal charging_fee;
    @TableField(exist = false)
    private  BigDecimal appoint_fee;
    private String parking_fee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @TableField(exist = false)
    private String ports_count;
    @TableField(exist = false)
    private String thumbnails;
    @TableField(exist = false)
    private List<String> thumbnailList;
    @TableField(exist = false)
    private List<CollectionsDTO> collections;

}


