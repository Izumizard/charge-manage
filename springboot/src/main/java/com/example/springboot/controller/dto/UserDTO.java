package com.example.springboot.controller.dto;

import com.example.springboot.entity.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能：用户表与余额表
 * 日期：2023/11/12 19:34
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String avatar;
    private String role;
    private String token;
    private BigDecimal amount;
    public UserDTO(User user,BigDecimal amount,String avatar) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.avatar = avatar;
        this.role = user.getRole();
        this.token = user.getToken();
        this.amount = amount;
    }
}


