package com.example.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：
 * 日期：2024/1/16 23:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collections")
public class CollectionsDTO {
    private Integer station_id;
    private Integer user_id;
    // 取消:0  收藏:1
    private Boolean status;
}
