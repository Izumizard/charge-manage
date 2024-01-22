package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.ChargingPort;


public interface ChargingPortMapper extends BaseMapper<ChargingPort> {
    IPage<ChargingPort> getPortsByPage(Page<ChargingPort> page, String station_name, String port_status);
}
