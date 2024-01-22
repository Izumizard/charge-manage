package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.config.Result;
import com.example.springboot.entity.ElectricityPrice;
import com.example.springboot.entity.StationThumbnail;
import com.example.springboot.mapper.StationThumbnailMapper;
import com.example.springboot.service.StationThumbnailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：
 * 日期：2023/11/21 0:28
 */
@RestController
@RequestMapping("/thumbnails")
public class StationThumbnailController {
    @Resource
    StationThumbnailService thumbnailService;
    @Resource
    StationThumbnailMapper thumbnailMapper;

    /**
     * 新增缩略图信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody StationThumbnail thumbnail) {
        try {
            thumbnailService.save(thumbnail);
        } catch (Exception e) {
            Result.error("系统错误！");
        }
        return Result.success("添加成功！");
    }

    /**
     * 修改缩略图信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody StationThumbnail thumbnail) {
        thumbnailService.updateById(thumbnail);
        return Result.success("修改成功！");
    }

    /**
     * 删除缩略图信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        thumbnailService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除缩略图信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        thumbnailService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 分页条件模糊查询站点缩略图信息
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(required = false) String station_name) {
        IPage<StationThumbnail> page = thumbnailMapper.selectByStationName(new Page<>(pageNum, pageSize), station_name);
        return Result.success(page);
    }
}
