package com.example.springboot.exception;

/**
 * 功能：
 * 日期：2023/9/22 20:43
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String msg){
        super(msg);
    }
}
