package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Orders;

import java.util.List;


public interface OrdersMapper extends BaseMapper<Orders> {
    String getStationName(Integer port_id);
    List<Orders> getInfo(Integer user_id);
    Orders selectOrder(Integer user_id, String port_number, String status);
    IPage<Orders> getOrdersByPage(Page<Orders> page, String username);
}
