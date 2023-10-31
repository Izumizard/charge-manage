package com.example.springboot;

import com.example.springboot.utils.JasyptEncryptorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 功能：使用JasyptEncryptor加密或解密数据库账号、地址、密码，邮箱配置M密码
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
        String emailcode = jasyptEncryptorUtils.encode("uoxrqssvifnubfbe");
        System.out.println("数据库地址加密为：" + datasource);
        System.out.println("-----------------------------");
        System.out.println("数据库用户加密为：" + username);
        System.out.println("-----------------------------");
        System.out.println("数据库地址密码加密为：" + password);
        System.out.println("-----------------------------");
        System.out.println("邮箱授权码加密为：" + emailcode);
        System.out.println("-----------------------------");
    }

    /**
     * 配置文件解密
     * */
    @Test
    void decode(){
        String datasource = jasyptEncryptorUtils.decode("DDqWeg+t+wdc1Cg+tr+bY9MIFRFizK9tZmQhS54kQ4EEyWqnkt/oXyeDC7dUJsLYwSdJYF6Y8pmtTg76OFBlKiReqnGV/aB+UQJiFH/sRpUrkCvlxqkT0pwtoRDIEDPnE/uh6bMrjrsWbr+LHSacAl+S6oS9gjHsGSnU7hLKtnM=");
        String username = jasyptEncryptorUtils.decode("Ytb4xDhtkG9HSXDFWvI/2g==");
        String password = jasyptEncryptorUtils.decode("7zEMcH+sko7+o/fBBaNdGA==");
        String emailcode = jasyptEncryptorUtils.decode("U5hF6Jgkwo69Ln3Ir+RK2xeCF74wd8U9j8E0NHqTzr8=");
        System.out.println("数据库地址解密为：" + datasource);
        System.out.println("-----------------------------");
        System.out.println("数据库用户解密为：" + username);
        System.out.println("-----------------------------");
        System.out.println("数据库地址密码解密为：" + password);
        System.out.println("-----------------------------");
        System.out.println("邮箱授权码加密为：" + emailcode);
        System.out.println("-----------------------------");
    }

}
