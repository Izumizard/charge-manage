package com.example.springboot.controller;

import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.entity.ElectricityPrice;
import com.example.springboot.service.ElectricityPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：
 * 日期：2023/11/21 0:19
 */
@RestController
@RequestMapping("/electricity")
public class ElectricityPriceController {
    @Resource
    ElectricityPriceService electricityPriceService;

    @AuthAccess
    @GetMapping("/getPriceByStationId/{stationId}")
    public List<ElectricityPrice> getPriceByStationId(@PathVariable Integer stationId) {
        return electricityPriceService.getPriceByStationId(stationId);
    }
}
