package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.ChargingStation;
import com.example.springboot.mapper.ChargingStationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：
 * 日期：2023/11/21 0:09
 */
@Service
public class ChargingStationService extends ServiceImpl<ChargingStationMapper, ChargingStation> {

    @Resource
    ChargingStationMapper chargingStationMapper;

    //条件查询站点信息、当前时间段电价信息、站点充电桩数量
    public List<ChargingStation> selectChargingStation(String station_name) {
        return chargingStationMapper.selectChargingStation(station_name);
    }

    //查询全部站点信息
    public List<ChargingStation> searchAll() {
        return  chargingStationMapper.searchAll();
    }

    //鉴权 查询全部站点信息
    public List<ChargingStation> selectByStation() {
        QueryWrapper<ChargingStation> queryWrapper = new QueryWrapper<>();
        return chargingStationMapper.selectList(queryWrapper);
    }
    //查询用户收藏站点信息
    public List<ChargingStation> searchStations(Integer user_id) {
        return  chargingStationMapper.searchStations(user_id);
    }
}
