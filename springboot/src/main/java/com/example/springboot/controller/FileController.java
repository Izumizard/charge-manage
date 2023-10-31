package com.example.springboot.controller;


import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能：File上传、下载、导出
 * 日期：2023/10/14 19:07
 */

@RestController

@RequestMapping("/file")
public class FileController {

    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator +"files"; //文件存储目录 D:\vue\files
    /**
     * 用户上传接口
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //文件的原始名称
        String mainName = FileUtil.mainName(originalFilename); //123
        String extName = FileUtil.extName(originalFilename); //png
        if (!FileUtil.exist(ROOT_PATH)){
            FileUtil.mkdir(ROOT_PATH); //如果当前文件的父级目录不存在，就创建
        }
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)){  //如果当前上传的文件已经存在了，那么这时候就在文件名前加个时间戳，重名文件名称
            //获得当前时间戳
            Date currentTime = new Date();
            //设置时间解析与格式化的形式
            //注意：yyyyMMddHHmmss里面不能有 - : . 等
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            originalFilename = dateString +"_" + mainName + "." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile); // 存储文件到本地磁盘里去
        String url = "http://" + ip +":" + port + "/file/download/" + originalFilename ;
        return Result.success(url);//返回文件连接，这个连接就是文件的下载地址
    }

    /**
     * 用户下载接口
     */
    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH +  File.separator + fileName;
        if(!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream= response.getOutputStream();
        outputStream.write(bytes); //数组是一个字节数组，也就是文件的字节流数组
        outputStream.flush();
        outputStream.close();
    }
}
