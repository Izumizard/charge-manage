package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.PriceDTO;
import com.example.springboot.entity.ElectricityPrice;

import java.util.List;


public interface ElectricityPriceMapper extends BaseMapper<ElectricityPrice> {
    List<PriceDTO> getPrice(Integer station_id);
    IPage<ElectricityPrice> getPriceByPage(Page<ElectricityPrice> page, String station_name);
}
