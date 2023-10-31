package com.example.springboot.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Validation;
import com.example.springboot.mapper.ValidationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能：封装对验证码信息进行持久化操作的方法
 * 日期：2023/10/23 18:43
 */
@Service
public class ValidationService extends ServiceImpl<ValidationMapper, Validation> {

    @Transactional
    public void saveCode(String email, String code, Integer type, DateTime expireDate) {

        Validation validation = new Validation();
        validation.setEmail(email);
        validation.setCode(code);
        validation.setType(type);
        validation.setTime(expireDate);

        // 删除同类型的验证
        UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
        validationUpdateWrapper.eq("email", email);
        validationUpdateWrapper.eq("type", type);
        remove(validationUpdateWrapper);

        // 再插入新的code
        save(validation);
    }

}
