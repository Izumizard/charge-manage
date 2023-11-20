package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：收藏实体类
 * 日期：2024/1/16 18:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collections")
public class Collections {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer user_id;
    private Integer station_id;
    // 取消:0  收藏:1
    private Boolean status;
}
