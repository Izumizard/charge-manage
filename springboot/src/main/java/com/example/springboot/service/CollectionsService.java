package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Collections;
import com.example.springboot.mapper.CollectionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能：封装对小程序收藏功能进行持久化操作的方法
 * 日期：2024/1/16 19:18
 */
@Service
public class CollectionsService  extends ServiceImpl<CollectionsMapper, Collections>{
    @Resource
    CollectionsMapper collectionsMapper;

    public void collectionsStatus(Integer user_id, Integer station_id, Boolean status) {
        // 根据 user_id 和 station_id 查询是否已经存在对应的收藏记录
        QueryWrapper<Collections> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("station_id", station_id);
        Collections existingCollection = collectionsMapper.selectOne(queryWrapper);
        if (existingCollection == null) {
            Collections collection = new Collections();
            collection.setUser_id(user_id);
            collection.setStation_id(station_id);
            collection.setStatus(true);
            collectionsMapper.insert(collection);
        } else {
            existingCollection.setStatus(status);
            collectionsMapper.updateById(existingCollection);
        }
    }
}
