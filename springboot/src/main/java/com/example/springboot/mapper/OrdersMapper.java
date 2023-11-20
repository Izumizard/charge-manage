package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Orders;

import java.util.List;


public interface OrdersMapper extends BaseMapper<Orders> {
    String getStationName(Integer port_id);
    List<Orders> getInfo(Integer user_id);
}
