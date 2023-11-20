package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.entity.Notice;
import com.example.springboot.entity.User;
import com.example.springboot.service.NoticeService;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：
 * 日期：2023/11/29 13:57
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;
    @Autowired
    UserService userService;

    /**
     * 新增通知信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        User currentUser = TokenUtils.getCurrentUser();
        assert currentUser != null;
        notice.setUserid(currentUser.getId());
        notice.setTime(DateUtil.now());
        noticeService.save(notice);
        return Result.success("添加成功！");
    }


    /**
     * 修改通知信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success("修改成功！");
    }

    /**
     * 删除通知信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除通知信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        noticeService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询全部通知信息
     */
    @AuthAccess
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> noticeList = noticeService.list(new QueryWrapper<Notice>().orderByAsc("id"));
        return Result.success(noticeList);
    }

    /**
     * 查询用户通知信息
     */
    @AuthAccess
    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");
        queryWrapper.eq("open", 1);
        List<Notice> noticeList = noticeService.list(queryWrapper);
        return Result.success(noticeList);
    }

    /**
     * ID查询通知信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice  = noticeService.getById(id);
        User user = userService.getById(id);
        if (user != null) {
            notice.setUser(user.getUsername());
        }
        return Result.success(notice);
    }



    /**
     * 分页多条件模糊查询通知信息
     * pageNum 是当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String title
    ) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
        Page<Notice> page = noticeService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Notice> records = page.getRecords();
        for (Notice record : records) {
            Integer id = record.getUserid();
            User user = userService.getById(id);
            if (user != null) {
                record.setUser(user.getName());
            }
        }
        return Result.success(page);
    }

    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String title,
                           @RequestParam(required = false) String content,
                           @RequestParam(required = false) String ids, // 1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);


        List<Notice> list;
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)){   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
            queryWrapper.like(StrUtil.isNotBlank(content), "content", content);
        }
        list = noticeService.list(queryWrapper); // 查询出当前notice表的所有数据
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
