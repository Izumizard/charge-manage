package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.entity.ChargingPort;
import com.example.springboot.mapper.ChargingPortMapper;
import com.example.springboot.service.ChargingPortService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 功能：
 * 日期：2023/11/21 0:21
 */
@RestController
@RequestMapping("/port")
public class ChargingPortController {

    @Resource
    ChargingPortService chargingPortService;
    @Resource
    ChargingPortMapper chargingPortMapper;


    /**
     * 新增电桩信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody ChargingPort port) {
        try {
            if (Objects.equals(port.getPort_status(), "空闲中")){
                port.setUser_Id(0);
            } else {
                port.setUser_Id(-1);
            }
            chargingPortService.save(port);
        } catch (Exception e) {
            Result.error("系统错误！");
        }
        return Result.success("添加成功！");
    }


    /**
     * 修改充电桩信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody ChargingPort chargingPort) {
        chargingPortService.updateById(chargingPort);
        return Result.success("修改成功！");
    }


    /**
     * 删除充电桩信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        chargingPortService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除充电桩信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        chargingPortService.removeBatchByIds(ids);
        return Result.success();
    }


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
     * 根据port表中的user_id查询充电桩是否被预约并使用
     */
    @PostMapping("/getPortStatus")
    public Result getPortStatus(@RequestBody ChargingPort port) {
        chargingPortService.useOrder(port.getPort_number());
        return Result.success("充电桩使用成功！");
    }


    /**
     * 分页多条件模糊查询充电桩信息
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(required = false) String station_name,
                               @RequestParam(required = false) String port_status) {
        IPage<ChargingPort> page = chargingPortMapper.getPortsByPage(new Page<>(pageNum, pageSize), station_name, port_status);
        return Result.success(page);
    }


}
