package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：
 * 日期：2023/11/20 23:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("charging_port")
public class ChargingPort {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer charging_station_id;
    private String port_status;
    private  
}
