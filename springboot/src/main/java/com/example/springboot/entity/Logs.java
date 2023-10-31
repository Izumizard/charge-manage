package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能：Logs实体类
 * 日期：2023/11/8 21:06
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logs {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String operation;
    private String type;
    private String ip;
    private String user;
    private String time;
}
