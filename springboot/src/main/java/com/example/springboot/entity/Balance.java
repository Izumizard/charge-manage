package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
* 功能：余额实体类
* 日期：2023/11/12 17:57
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("balance")
public class Balance {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer user_id;
    private BigDecimal amount;
    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
}


