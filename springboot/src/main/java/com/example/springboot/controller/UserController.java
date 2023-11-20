package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.annotations.MessageLogs;
import com.example.springboot.common.constants.Contants;
import com.example.springboot.common.config.Result;
import com.example.springboot.common.enums.LogType;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.Balance;
import com.example.springboot.entity.User;
import com.example.springboot.entity.Validation;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.BalanceService;
import com.example.springboot.service.UserService;
import com.example.springboot.service.ValidationService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能：User控制
 * 日期：2023/9/15 14:17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    @Resource
    private BalanceService balanceService;
    @Resource
    private ValidationService validationService;

    /**
     * 新增用户信息
     */
    @MessageLogs(operation = "用户信息", type = LogType.ADD)
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

        return Result.success("添加用户成功！");
    }


    /**
     * 修改用户信息
     */
    @MessageLogs(operation = "用户信息", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("修改成功！");
    }

    /**
     * 删除用户信息
     */
    @MessageLogs(operation = "用户信息", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if (id.equals(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeById(id);
        balanceService.removeBalanceByUserId(id);
        return Result.success();
    }

    /**
     * 批量删除用户信息
     */
    @MessageLogs(operation = "用户信息", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }

        userService.removeBatchByIds(ids);
        balanceService.removeBalancesByUserIds(ids);
        return Result.success();
    }

    /**
     * 查询全部用户信息
     */
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
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam(required = false) String name
                               ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByAsc("id");
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        //select * from user who username like '%#{username}%' and name like '%#{name}%'
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 构建 UserDTO 列表
        List<UserDTO> userList = new ArrayList<>();
        for (User user : page.getRecords()) {
            Balance balance = balanceService.selectByUserId(user.getId());
            BigDecimal amount = (balance != null) ? balance.getAmount() : BigDecimal.ZERO; // 如果余额记录不存在，则将余额设为 0
            String avatar = (StrUtil.isNotBlank(user.getAvatar())) ? user.getAvatar() :
                    "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";
            UserDTO userDTO= new UserDTO(user, amount, avatar);
            userList.add(userDTO);
        }
        // 构建新的分页对象
        Page<UserDTO> resultPage = new Page<>();
        resultPage.setRecords(userList);
        resultPage.setTotal(page.getTotal());
        resultPage.setCurrent(pageNum);
        resultPage.setSize(pageSize);
        return Result.success(resultPage);
    }

    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String ids, // 1,2,3,4,5
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);


        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)){   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }
        list = userService.list(queryWrapper); // 查询出当前User表的所有数据
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
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
        List<User> userList = reader.readAll(User.class);

        //写入数据到数据库
        try {
            userService.saveBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("数据批量导入出错！");
        }

        return Result.success();
    }

    /**
     *用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            return Result.error("用户名或密码不能为空");
        }
        Map<String, Object> loginResult = userService.login(user);
        User dbUser = (User) loginResult.get("user");
        BigDecimal userBalance = (BigDecimal) loginResult.get("balance");
        String avatar = dbUser.getAvatar();
        UserDTO response = new UserDTO(dbUser, userBalance, avatar);
        return  Result.success(response);
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

        // 重置密码
        user.setPassword("123456");
        userService.updateById(user);
        return Result.success();
    }
}
