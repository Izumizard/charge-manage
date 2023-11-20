package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.entity.Swiper;
import com.example.springboot.service.SwiperService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：swiper控制
 * 日期：2023/9/15 14:17
 */
@RestController
@RequestMapping("/swiper")
public class SwiperController {

    @Resource
    SwiperService swiperService;


    /**
     * 新增轮播图信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody Swiper swiper) {
        try {
            swiperService.save(swiper);
        } catch (Exception e) {
                Result.error("系统错误！");
        }

        return Result.success("添加成功！");
    }


    /**
     * 修改轮播图信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Swiper swiper) {
        swiperService.updateById(swiper);
        return Result.success("修改成功！");
    }

    /**
     * 删除轮播图信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        swiperService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除轮播图信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        swiperService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询全部轮播图信息
     */
    @AuthAccess
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Swiper> swiperList = swiperService.list(new QueryWrapper<Swiper>().orderByAsc("id"));
        return Result.success(swiperList);
    }

    /**
     * ID查询轮播图信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Swiper swiper  = swiperService.getById(id);
        return Result.success(swiper);
    }



    /**
     * 分页多条件模糊查询轮播图信息
     * pageNum 是当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String banner
                               ) {
        QueryWrapper<Swiper> queryWrapper = new QueryWrapper<Swiper>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(banner), "banner", banner);
        Page<Swiper> page = swiperService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String banner,
                           @RequestParam(required = false) String topic,
                           @RequestParam(required = false) String ids, // 1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);


        List<Swiper> list;
        QueryWrapper<Swiper> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)){   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(banner), "banner", banner);
            queryWrapper.like(StrUtil.isNotBlank(topic), "topic", topic);
        }
        list = swiperService.list(queryWrapper); // 查询出当前swiper表的所有数据
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("轮播图信息", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }





}
