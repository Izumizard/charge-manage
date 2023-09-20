package com.example.springboot.common;

import lombok.Data;

import java.util.List;

/**
 * 功能：Page类
 * 日期：2023/9/21 18:18
 */

@Data
public class Page<T> {
    private Integer total;
    private List<T> list;
}
