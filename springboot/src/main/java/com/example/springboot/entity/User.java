package com.example.springboot.entity;



import lombok.Data;

/**
 * 功能：User实体类 与数据库表对应
 * 日期：2023/9/13 19:18
 */

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String avatar;
}
