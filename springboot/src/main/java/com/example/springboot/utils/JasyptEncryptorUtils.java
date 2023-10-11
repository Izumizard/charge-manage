package com.example.springboot.utils;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能：JasyptEncryptor工具类
 * 日期：2023/10/16 17:32
 */

@Component
public class JasyptEncryptorUtils {

    @Autowired
    private StringEncryptor encryptor;

    @Value("easygo")
    private String encrytorPassword;



    /**
     * 明文加密
     * @param plaintext 明文
     * @return 密文
     */
    public String encode(String plaintext){
            return  encryptor.encrypt(plaintext);
    }



    /**
     * 密文解密
     * @param ciphertext 密文
     * @return 明文
     */
    public String decode(String ciphertext){
        return  encryptor.decrypt(ciphertext);
    }



    /**
     * 获取加密器
     * @return StringEncryptor 对象
     */
    public StringEncryptor getEncryptor() {
        StandardPBEStringEncryptor pbeStringEncryptor = new StandardPBEStringEncryptor();
        pbeStringEncryptor.setPassword(encrytorPassword);
        return pbeStringEncryptor;
    }

}
