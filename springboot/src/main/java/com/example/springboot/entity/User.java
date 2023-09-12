package com.example.springboot.entity;



import lombok.Data;

/**
 * 功能：数据库User表接口
 * 日期：2023/9/13 19:18
 */

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String avatar;
}
