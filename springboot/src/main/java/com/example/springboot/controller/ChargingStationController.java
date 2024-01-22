package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.annotations.MessageLogs;
import com.example.springboot.common.config.Result;
import com.example.springboot.common.enums.LogType;
import com.example.springboot.controller.dto.CollectionsDTO;
import com.example.springboot.entity.ChargingStation;
import com.example.springboot.service.ChargingStationService;
import com.example.springboot.service.CollectionsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：
 * 日期：2023/11/21 0:22
 */
@RestController
@RequestMapping("/station")
public class ChargingStationController {

    @Resource
    ChargingStationService chargingStationService;
    @Resource
    CollectionsService collectionsService;

    /**
     * 新增站点信息
     */
    @MessageLogs(operation = "站点信息", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody ChargingStation chargingStation) {
        try {
            chargingStationService.save(chargingStation);
        } catch (Exception e) {
            Result.error("系统错误！");
        }

        return Result.success("添加成功！");
    }


    /**
     * 修改站点信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody ChargingStation chargingStation) {
        chargingStationService.updateById(chargingStation);
        return Result.success("修改成功！");
    }
    /**
     * 删除站点信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        chargingStationService.removeById(id);

        return Result.success();
    }

    /**
     * 批量删除站点信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        chargingStationService.removeBatchByIds(ids);
        return Result.success();
    }


    /**
     * 查询全部站点信息
     */
    @AuthAccess
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<ChargingStation> stationList = chargingStationService.searchAll();
        for (ChargingStation chargingStation : stationList) {
            String thumbnails = chargingStation.getThumbnails();
            if (thumbnails != null) {
                String[] thumbnailsArray = thumbnails.split(",");
                for (int i = 0; i < thumbnailsArray.length; i++) {
                    thumbnailsArray[i] = thumbnailsArray[i].replace("|", ",");
                }
                List<String> thumbnailList = new ArrayList<>(Arrays.asList(thumbnailsArray));
                chargingStation.setThumbnailList(thumbnailList);
            }

        }
        return Result.success(stationList);
    }



    /**
     * 鉴权 查询全部站点信息
     */
    @GetMapping("/listOfStation")
    public Result selectByStations() {
        List<ChargingStation> stationList = chargingStationService.selectByStation();
        return Result.success(stationList);
    }



    /**
     * 查询用户收藏的站点
     * @param user_id
     * @return
     */
    @GetMapping("/selectByUserId/{user_id}")
    public Result selectByUserId(@PathVariable Integer user_id){
        List<ChargingStation> stationsList = chargingStationService.searchStations(user_id);
        for (ChargingStation station : stationsList) {
            String thumbnails = station.getThumbnails();
            if (thumbnails != null) {
                String[] thumbnailsArray = thumbnails.split(",");
                for (int i = 0; i < thumbnailsArray.length; i++) {
                    thumbnailsArray[i] = thumbnailsArray[i].replace("|", ",");
                }
                List<String> thumbnailList = new ArrayList<>(Arrays.asList(thumbnailsArray));
                station.setThumbnailList(thumbnailList);
            }
        }
        return Result.success(stationsList);
    }



    /**
     * ID查询站点信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ChargingStation chargingStation = chargingStationService.getById(id);
        return Result.success(chargingStation);
    }


    /**
     * 根据user_id更新站点收藏状态
     */
    @PostMapping("/collections/status")
    public Result collectionStatus(@RequestBody CollectionsDTO collections) {
        collectionsService.collectionsStatus(collections.getUser_id(), collections.getStation_id()
                , collections.getStatus());
        return Result.success("站点收藏状态已更新！");
    }


    /**
     * 分页多条件模糊查询站点信息
     * pageNum 是当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String station_name) {
        QueryWrapper<ChargingStation> queryWrapper = new QueryWrapper<ChargingStation>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(station_name), "station_name", station_name);
        Page<ChargingStation> page = chargingStationService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 获取站点信息及额外字段
        List<ChargingStation> chargingStations = page.getRecords();
        List<ChargingStation> chargingStationInfoList = chargingStationService.selectChargingStation(station_name);
        for (ChargingStation chargingStation : chargingStations) {
            for (ChargingStation chargingStationInfo : chargingStationInfoList) {
                if (chargingStation.getId().equals(chargingStationInfo.getId())) {
                    chargingStation.setPorts_count(chargingStationInfo.getPorts_count());
                    chargingStation.setCharging_fee(chargingStationInfo.getCharging_fee());
                    String thumbnails = chargingStationInfo.getThumbnails();
                    if (thumbnails != null) {
                        String[] thumbnailsArray = thumbnails.split(",");
                        for (int i = 0; i < thumbnailsArray.length; i++) {
                            thumbnailsArray[i] = thumbnailsArray[i].replace("|", ",");
                        }
                        List<String> thumbnailList = new ArrayList<>(Arrays.asList(thumbnailsArray));
                        chargingStation.setThumbnailList(thumbnailList);
                        break;
                    }
                }
            }
        }

        return Result.success(page);
    }

    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String station_name,
                           @RequestParam(required = false) String station_status,
                           @RequestParam(required = false) String ids, // 1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);


        List<ChargingStation> list;
        QueryWrapper<ChargingStation> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(station_name), "station_name", station_name);
        }
        list = chargingStationService.list(queryWrapper); // 查询出当前charging_station表的所有数据
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("充电站点表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }


    /**
     * 批量导入
     * @param file 传入的excel文件对象
     * @return  导出结果
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<ChargingStation> stationList = reader.readAll(ChargingStation.class);

        //写入数据到数据库
        try {
            chargingStationService.saveBatch(stationList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("数据批量导入出错！");
        }
        return Result.success();
    }

}
