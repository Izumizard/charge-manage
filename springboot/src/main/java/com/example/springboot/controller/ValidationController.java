package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Validation;
import com.example.springboot.service.ValidationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能：
 * 日期：2023/10/26 15:43
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {
    @Resource
    private ValidationService validationService;
    private final String now = DateUtil.now();

    @AuthAccess
    @PostMapping("/add")
    public Result save(@RequestBody Validation validation) {
            // 更新操作
        validationService.saveOrUpdate(validation);
        return Result.success();
    }

    @DeleteMapping("delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        validationService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(validationService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(validationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Validation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        // 分页查询
        Page<Validation> page = new Page<>(pageNum, pageSize);
        IPage<Validation> validationPage;
        validationPage = validationService.page(page, queryWrapper);

        return Result.success(validationPage);
    }
}
