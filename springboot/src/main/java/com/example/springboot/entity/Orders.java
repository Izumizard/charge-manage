package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：
 * 日期：2023/12/29 16:28
 */
@Data

@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Orders implements Serializable {
    @TableId(type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    @Alias("订单号")
    private String order_no;
    @TableField(exist = false)
    private transient String username;
    @TableField(exist = false)
    private transient Integer station_id;
    @TableField(exist = false)
    private transient String avatar;
    @TableField(exist = false)
    @Alias("充电桩序号")
    private String port_number;
    @Alias("充电站名称")
    private String station_name;
    private Integer user_id;
    private Integer port_id;
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
    @Alias("预约时长")
    private Integer duration;
}
