package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.enums.ValidationEnum;
import com.example.springboot.entity.Balance;
import com.example.springboot.entity.User;
import com.example.springboot.entity.Validation;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BalanceMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.util.*;

import static com.example.springboot.common.constants.Contants.CODE_600;


/**
 * 功能：封装对用户信息进行持久化操作的方法
 * 新增：发送邮箱验证码、查询管理员信息
 * 日期：2023/9/14 21:38
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    UserMapper userMapper;
    @Resource
    BalanceMapper balanceMapper;

    @Override
    public boolean save(User entity) {
        if (StrUtil.isBlank(entity.getName())) {
            entity.setName(entity.getUsername());
        }
        if (StrUtil.isBlank(entity.getPassword())) {
            entity.setPassword("123456");  // 默认密码：123456
        }
        if (StrUtil.isBlank(entity.getRole())) {
            entity.setRole("用户");  // 默认角色：用户
        }
        if (super.save(entity) && "用户".equals(entity.getRole())) {
            // 如果用户角色为“用户”，在余额表中创建对应的记录并将金额设为 0
            Balance balance = new Balance();
            balance.setUserId(entity.getId()); // 设置用户ID
            balance.setAmount(BigDecimal.ZERO); // 设置余额初始值为 0
            balanceMapper.insert(balance); // 插入余额记录
        }
        return super.save(entity);
    }

    public User selectByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username); //eq => ==  where username = #{username}

        //根据用户名查询数据库的用户信息
        return getOne(queryWrapper);  // select * from user where username = #{username}
    }

    public User selectById(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return getOne(queryWrapper);
    }

    public List<User> selectByBlur(String username, String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username)
                .like(StringUtils.isNotBlank(name), "name", name);
        return userMapper.selectList(queryWrapper);
    }


    @Resource
    private ValidationService validationService;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;



    public void sendEmailCode(String email, Integer type) {

        Date now = new Date();
        // 先查询同类型code
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email", email);
        validationQueryWrapper.eq("type", type);
        validationQueryWrapper.ge("time", now);  // 查询数据库没过期的code
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation != null) {
            throw new ServiceException("-1", "当前您的验证码仍然有效，请不要重复发送");
        }

        String verifyCode = RandomUtil.randomNumbers(6);
        Context context = new Context(); // 引入Template的Context
        // 设置模板中的变量（分割验证码）
        context.setVariable("verifyCode", Arrays.asList(verifyCode.split("")));
        // 第一个参数为模板的名称(html不用写全路径)
        String process = templateEngine.process("EmailVerificationCode.html", context); // 这里不用写全路径
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("【易电充】验证码");
            message.setSentDate(now);

            message.setText(process, true);
        } catch (Exception e){
            throw new ServiceException(CODE_600,"邮件发送异常");
        }
        javaMailSender.send(mimeMessage);

        //验证码存入数据库
        validationService.saveCode(email, verifyCode, ValidationEnum.FORGET_PWD.getCode(), DateUtil.offsetMinute(now, 5));


    }

    //查询管理员
    public List<User> selectByAdmin() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "超级管理员");
        return userMapper.selectList(queryWrapper);
    }

    @Resource BalanceService balanceService;
    //验证用户账户是否合法
    public Map<String, Object> login(User user) {
       User dbUser = selectByUsername(user.getUsername());
       if (dbUser == null){
           //抛出一个自定义的异常
           throw new ServiceException("用户名或密码错误");
       }
       if (!user.getPassword().equals(dbUser.getPassword())){
           throw new ServiceException("用户名或密码错误");
       }
        Balance balance = balanceService.selectByUserId(dbUser.getId());
        BigDecimal userBalance = (balance != null) ? balance.getAmount() : BigDecimal.ZERO;


        // 将余额信息存储到用户对象中
        String token = TokenUtils.createToken(dbUser.getId().toString(),dbUser.getPassword());
        dbUser.setToken(token);

        Map<String, Object> result = new HashMap<>();
        result.put("user", dbUser);
        result.put("balance", userBalance);
        return result;
    }

    public User register(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser != null){
            //抛出一个自定义的异常
            throw new ServiceException("用户名已存在");
        }
        user.setRole("用户");  // 默认角色：用户
        //用户名即昵称
        user.setName(user.getUsername());
        userMapper.insert(user);
        // 在 balance 表中为新用户创建余额信息（默认为零）
        Balance balance = new Balance();
        balance.setUserId(user.getId());
        balance.setAmount(BigDecimal.ZERO);
        balanceMapper.insert(balance);
        return user;
    }

}
