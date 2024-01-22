package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.controller.dto.PriceDTO;
import com.example.springboot.controller.dto.PriceRequest;
import com.example.springboot.entity.ChargingPort;
import com.example.springboot.entity.ElectricityPrice;
import com.example.springboot.mapper.ElectricityPriceMapper;
import com.example.springboot.service.ElectricityPriceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 功能：
 * 日期：2023/11/21 0:19
 */
@RestController
@RequestMapping("/electricity")
public class ElectricityPriceController {
    @Resource
    ElectricityPriceService electricityPriceService;
    @Resource
    ElectricityPriceMapper electricityPriceMapper;

    /**
     * 新增电价信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody ElectricityPrice price) {
        try {
            electricityPriceService.save(price);
        } catch (Exception e) {
            Result.error("系统错误！");
        }
        return Result.success("添加成功！");
    }


    /**
     * 修改电价信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody ElectricityPrice price) {
        electricityPriceService.updateById(price);
        return Result.success("修改成功！");
    }


    /**
     * 删除电价信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        electricityPriceService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除电价信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        electricityPriceService.removeBatchByIds(ids);
        return Result.success();
    }

    @AuthAccess
    @GetMapping("/getPriceByStationId/{stationId}")
    public List<ElectricityPrice> getPriceByStationId(@PathVariable Integer stationId) {
        return electricityPriceService.getPriceByStationId(stationId);
    }

    @AuthAccess
    @GetMapping("/getPrice")
    public Result getPrice(@RequestParam Integer station_id) {
        List<PriceDTO> priceDTO= electricityPriceService.getPriceInfo(station_id);
        List<PriceRequest> prices = new ArrayList<>();
        for (PriceDTO priceDTOs : priceDTO) {
            PriceRequest price = new PriceRequest(priceDTOs.getStart(), priceDTOs.getEnd(), priceDTOs.getPrice());
            prices.add(price);
        }
        return Result.success(prices);
    }

    /**
     * 分页多条件模糊查询站点电价信息
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(required = false) String station_name) {
        IPage<ElectricityPrice> page = electricityPriceMapper.getPriceByPage(new Page<>(pageNum, pageSize), station_name);
        return Result.success(page);
    }
}
