package com.example.springboot.controller.dto;

import lombok.Data;

/**
 * 功能：用户表和验证码表
 * 日期：2023/10/23 18:21
 */
@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String email;
    private String code;
}
