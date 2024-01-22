package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.controller.dto.PriceDTO;
import com.example.springboot.entity.ElectricityPrice;
import com.example.springboot.mapper.ElectricityPriceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 功能：
 * 日期：2023/11/21 0:18
 */
@Service
public class ElectricityPriceService extends ServiceImpl<ElectricityPriceMapper, ElectricityPrice> {
    @Resource
    ElectricityPriceMapper electricityPriceMapper;

    public List<ElectricityPrice> getPriceByStationId(Integer stationId) {
        QueryWrapper<ElectricityPrice> wrapper = new QueryWrapper<>();
        wrapper.eq("station_id", stationId);
        List<ElectricityPrice> priceList = electricityPriceMapper.selectList(wrapper);
        for (ElectricityPrice price : priceList) {
            BigDecimal t_price = price.getE_price().add(price.getService_fee());
            price.setT_price(t_price);
        }

        return priceList;
    }

    public List<PriceDTO> getPriceInfo(Integer station_id) {
        return electricityPriceMapper.getPrice(station_id);
    }
}
