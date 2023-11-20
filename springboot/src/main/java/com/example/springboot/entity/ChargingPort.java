package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 功能：充电桩实体类
 * 日期：2023/11/20 23:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("charging_port")
public class ChargingPort {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //维修中：-1  空闲中：0 其他：user_id
    private Integer user_Id;
    private Integer station_id;
    private String port_status;
    private String port_number;
    private Double port_power;
    private Integer port_voltage;
    private String port_type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date p_create_time;
}
