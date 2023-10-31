package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能：User接口访问数据库
 * 日期：2023/9/13 19:18
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> selectByBlur(@Param("username") String username, @Param("name") String name);
    List<User> selectByAdmin();

}
