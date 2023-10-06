package com.example.springboot.controller;

import com.example.springboot.common.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能：
 * 日期：2023/9/15 14:17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 新增用户信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.insertUser(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("用户名已存在！");
            } else {
                Result.error("系统错误！");
            }

        }

        return Result.success("注册成功！");
    }


    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("修改成功！");
    }

    /**
     * 删除用户信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 批量删除用户信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        userService.batchDeleteUser(ids);
        return Result.success();
    }

    /**
     * 查询全部用户信息
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.selectAll();
        return Result.success(userList);
    }

    /**
     * ID查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user  = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * name查询用户信息
     * 如果不确定返回的结果会有几个，那就统一返回一个List对象机核
     *
     */
    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name) {
        List<User> userList = userService.selectByName(name);
        return Result.success(userList);
    }

    /**
     * 多条件查询用户信息
     */
    @GetMapping("/selectByMore")
    public Result selectByMore(@RequestParam String username, @RequestParam String name) {
        List<User> userList = userService.selectByMore(username,name);
        return Result.success(userList);
    }


    /**
     * 多条件模糊查询用户信息
     */
    @GetMapping("/selectByBlur")
    public Result selectByBlur(@RequestParam String username, @RequestParam String name) {
        List<User> userList = userService.selectByBlur(username,name);
        return Result.success(userList);
    }

    /**
     * 分页多条件模糊查询用户信息
     * pageNum 是当前的页码
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username, @RequestParam String name) {
        Page<User> page = userService.selectByPage(pageNum, pageSize, username, name);
        return Result.success(page);
    }
}
