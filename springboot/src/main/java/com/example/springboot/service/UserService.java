package com.example.springboot.service;

import com.example.springboot.common.Page;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 功能：封装对用户信息进行持久化操作的方法
 * 日期：2023/9/14 21:38
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void batchDeleteUser(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteUser(id);
        }
    }


    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public List<User> selectByMore(String username, String name) {
        return userMapper.selectByMore(username,name);
    }

    public List<User> selectByBlur(String username, String name) {
        return userMapper.selectByBlur(username,name);
    }

    public Page<User> selectByPage(Integer pageNum, Integer pageSize, String username, String name) {
        Integer skipNum =  (pageNum - 1) * pageSize; //计算出来

        Page<User> page =new Page<>();
        List<User> userList = userMapper.selectByPage(skipNum,pageSize,username,name);
        Integer total = userMapper.selectCountByPage(username, name);
        page.setTotal(total);
        page.setList(userList);
        return page;
    }


    //验证用户账户是否合法
    public User login(User user) {
        //根据用户名查询数据库的用户信息
       User dbUser = userMapper.selectByUsername(user.getUsername());
       if (dbUser == null){
           //抛出一个自定义的异常
           throw new ServiceException("用户名或密码错误");
       }
       if (!user.getPassword().equals(dbUser.getPassword())){
           throw new ServiceException("用户名或密码错误");
       }
        return dbUser;
    }
}