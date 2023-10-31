package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.RechargeRecords;
import com.example.springboot.service.RechargeRecordsService;
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
 * 功能：RechargeRecords控制
 * 日期：2023/11/16 14:28
 */
@RestController
@RequestMapping("/rechargeRecords")
public class RechargeRecordsController {


    @Resource
    RechargeRecordsService rechargeRecordsService;

    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String user_username,
                           @RequestParam(required = false) String ids, // 1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);


        List<RechargeRecords> list;
        QueryWrapper<RechargeRecords> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)){   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(user_username), "user_username", user_username);
        }
        list = rechargeRecordsService.list(queryWrapper); // 查询出当前表的所有数据
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("充值订单表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }



    /**
     * 分页多条件模糊查询充值订单信息
     * pageNum 是当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String user_username) {
        Page<RechargeRecords> page = new Page<>(pageNum, pageSize);
        IPage<RechargeRecords> records = rechargeRecordsService.getRechargeRecords(page, user_username);
        return  Result.success(records);
    }



    /**
     * 删除充值订单信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        rechargeRecordsService.removeById(id);
        return Result.success();
    }



    /**
     * 批量删除充值订单信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        rechargeRecordsService.removeBatchByIds(ids);
        return Result.success();
    }

}
