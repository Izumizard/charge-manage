package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.ElectricityPrice;
import com.example.springboot.entity.StationThumbnail;


public interface StationThumbnailMapper extends BaseMapper<StationThumbnail> {
    IPage<StationThumbnail> selectByStationName(Page<ElectricityPrice> page, String station_name);
}
