package com.example.springboot.controller;

import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.controller.dto.UsePortRequest;
import com.example.springboot.entity.ChargingPort;
import com.example.springboot.service.ChargingPortService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：
 * 日期：2023/11/21 0:21
 */
@RestController
@RequestMapping("/port")
public class ChargingPortController {

    @Resource
    ChargingPortService chargingPortService;


    /**
     * 根据站点ID查询站点中的充电桩信息
     */
    @AuthAccess
    @GetMapping("/selectByStationId/{stationId}")
    public Result selectByStationId(@PathVariable Integer stationId) {
        List<ChargingPort> chargingPorts = chargingPortService.findByStationId(stationId);
        return Result.success(chargingPorts);
    }



    /**
     * 根据port表中的user_id查询充电桩是否被预约
     */
    @AuthAccess
    @PostMapping("/getPortStatus")
    public Result getPortStatus(@RequestBody ChargingPort port) {
        chargingPortService.useOrder(port.getPort_number());
        return Result.success("充电桩使用成功！");
    }
}
