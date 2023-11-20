package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：充电站缩略图实体类
 * 日期：2023/11/20 23:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("station_thumbnail")
public class StationThumbnail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer station_id;
    private String thumbnail;
}
