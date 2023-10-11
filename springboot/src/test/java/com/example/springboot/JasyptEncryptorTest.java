package com.example.springboot;

import com.example.springboot.utils.JasyptEncryptorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 功能：使用JasyptEncryptor加密数据库账号、地址、密码
 * 日期：2023/10/16 19:33
 */


@SpringBootTest
class JasyptEncryptorTest {

    @Autowired
    public JasyptEncryptorUtils jasyptEncryptorUtils;

    @Test
    void  contextLoads(){

    }
    /**
     * 配置文件加密
     * */
    @Test
    void Jasypt(){
        String datasource = jasyptEncryptorUtils.encode("jdbc:mysql://${ip}:3306/charge_manage?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8");
        String username = jasyptEncryptorUtils.encode("root");
        String password = jasyptEncryptorUtils.encode("root");
        System.out.println("数据库地址加密为：" + datasource);
        System.out.println("-----------------------------");
        System.out.println("数据库用户加密为：" + username);
        System.out.println("-----------------------------");
        System.out.println("数据库地址密码加密为：" + password);
        System.out.println("-----------------------------");
    }

    /**
     * 配置文件解密
     * */
    @Test
    void decode(){
        String datasource = jasyptEncryptorUtils.decode("jjycvBxFkM6jXlVUCqUjUf3D4Tdsgo/wq0YaXS5Vg5DrpoaFiziPdYP5+cbgaCfwCGIU45XIrBpyTHB0LUoEFahGqTNZnuMYiJQwNzAbIvKlwTBVYkvgxrnF7rYowWj/+YnloorwUhCywa4O30joFRzQj6UEayiOX1Zn7ZdS51w=");
        String username = jasyptEncryptorUtils.decode("wLtYNUl9FKWaWi9pr8vaug==");
        String password = jasyptEncryptorUtils.decode("2H4S6y1D+sTJwhOqgaoeyA==");
        System.out.println("数据库地址解密为：" + datasource);
        System.out.println("-----------------------------");
        System.out.println("数据库用户解密为：" + username);
        System.out.println("-----------------------------");
        System.out.println("数据库地址密码解密为：" + password);
        System.out.println("-----------------------------");
    }

}
