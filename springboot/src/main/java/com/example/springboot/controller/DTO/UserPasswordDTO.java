package com.example.springboot.controller.DTO;

import lombok.Data;

/**
 * 功能：
 * 日期：2023/10/23 18:21
 */
@Data
public class UserPasswordDTO {
    private String username;
    private String phone;
    private String password;
    private String email;
    private String code;
}
