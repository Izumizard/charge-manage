package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.ChargingStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ChargingStationMapper extends BaseMapper<ChargingStation> {
    List<ChargingStation> selectChargingStation(@Param("station_name") String station_name);
    List<ChargingStation> searchAll();
    List<ChargingStation> searchStations(@Param("user_id") Integer userId);
}
