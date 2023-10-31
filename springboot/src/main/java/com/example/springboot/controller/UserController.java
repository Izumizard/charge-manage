package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Contants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.DTO.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.example.springboot.entity.Validation;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import com.example.springboot.service.ValidationService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 功能：User控制
 * 日期：2023/9/15 14:17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Resource
    private ValidationService validationService;

    /**
     * 新增用户信息
     */
    @AuthAccess
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.save(user);
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
        userService.updateById(user);
        return Result.success("修改成功！");
    }

    /**
     * 删除用户信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if (id.equals(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除用户信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询全部用户信息
     */
    @AuthAccess
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.list(new QueryWrapper<User>().orderByAsc("id"));
        return Result.success(userList);
    }

    /**
     * ID查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user  = userService.getById(id);
        return Result.success(user);
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
                               @RequestParam String username
                               //@RequestParam String name
                               ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        //queryWrapper.like(StrUtil.isNotBlank(username), "name", name);
        //select * from user who username like '%#{username}%' and name like '%#{name}%'
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     *用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            return Result.error("用户名或密码不能为空");
        }
        user = userService.login(user);
        return  Result.success(user);
    }

    /**
     *用户注册接口
     */
    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            return Result.error("用户名或密码不能为空");
        }
        user = userService.register(user);
        return  Result.success(user);
    }

    /**
     *管理员查询接口
     */
    @AuthAccess
    @GetMapping("/listOfAdmin")
    public Result selectByAdmin() {
        List<User> userList = userService.selectByAdmin();
        return Result.success(userList);
    }


    /**
     *  发送邮箱验证码
     */
    @AuthAccess
    @GetMapping("/email/{email}/{type}")
    public Result sendEmailCode(@PathVariable String email, @PathVariable Integer type)  {
        if(StrUtil.isBlank(email)) {
            throw new ServiceException(Contants.CODE_400, "参数错误");
        }
        if(type == null) {
            throw new ServiceException(Contants.CODE_400, "参数错误");
        }
        userService.sendEmailCode(email, type);
        return Result.success();
    }

    /**
     * 忘记密码 | 重置密码
     */
    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getEmail()) || StrUtil.isBlank(userPasswordDTO.getCode())) {
            throw new ServiceException("-1", "参数异常");
        }
        // 先查询 邮箱验证的表，看看之前有没有发送过  邮箱code，如果不存在，就重新获取
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email", userPasswordDTO.getEmail());
        validationQueryWrapper.eq("code", userPasswordDTO.getCode());
        validationQueryWrapper.ge("time", new Date());  // 查询数据库没过期的code, where time >= new Date()
        Validation one = validationService.getOne(validationQueryWrapper);
        if (one == null) {
            throw new ServiceException("-1", "验证码过期，请重新获取");
        }

        // 如果验证通过了，就查询要不过户的信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", userPasswordDTO.getEmail());  //存根据email查询用户信息
        User user = userService.getOne(userQueryWrapper);

        /**
         * 重置密码
         */
        user.setPassword("123456");
        userService.updateById(user);
        return Result.success();
    }
}
