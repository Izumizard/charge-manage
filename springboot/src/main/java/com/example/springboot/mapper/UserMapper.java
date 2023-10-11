package com.example.springboot.mapper;

import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 功能：User接口访问数据库
 * 日期：2023/9/13 19:18
 */
@Mapper
public interface UserMapper {

    @Insert("insert into `user`(username,password,name,phone,email,address,avatar,role) " +
            "values(#{username}, #{password}, #{name}, #{phone}, #{email},#{address}, #{avatar}, #{role})")
    void insert(User user);


    @Update("update `user` set username = #{username},name = #{name},phone = #{phone}," +
            " email = #{email}, address = #{address}, avatar = #{avatar}, role = #{role} where id = #{id}")
    void updateUser(User user);


    @Delete("delete from `user` where id = #{id}")
    void deleteUser(Integer id);


    @Select("select * from `user` order by id")
    List<User> selectAll();


    @Select("select * from `user` where id = #{id} order by id desc")
    User selectById(Integer id);


    @Select("select * from `user` where name = #{name} order by id desc")
    List<User> selectByName(String name);

    @Select("select * from `user` where username = #{username} and name = #{name} order by id desc")
    List<User> selectByMore(@Param("username") String username,@Param("name") String name);

    @Select("select * from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc")
    List<User> selectByBlur(String username, String name);

    @Select("select * from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc limit #{skipNum},#{pageSize}")
    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize, @Param("username") String username, @Param("name") String name);

    @Select("select count(id) from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc")
    int selectCountByPage(@Param("username") String username, @Param("name") String name);

    @Select("select * from `user` where username = #{username} order by id desc")
    User selectByUsername(String username);

    @Select("select id, name, username, role from `user` where role = '超级管理员' order by id desc")
    List<User> selectByAdmin(String role);
}
