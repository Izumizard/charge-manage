package com.example.springboot.entity;



import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：Swiper实体类 与数据库表对应
 * 日期：2023/9/13 19:18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("swiper")
public class Swiper {
    @TableId(type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    @Alias("轮播图标题")
    private String topic;
    @Alias("轮播图路径")
    private String banner;
    @Alias("是否展示")
    private Boolean status;
    @Alias("轮播图描述")
    private String description;

}
