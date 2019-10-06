package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.service.FileService;
import com.mmall.util.FTPUtil;
import com.mmall.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("fileService")
public class FileServiceImpl implements FileService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    public ServerResponse fileUp(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();                            //文件名
        String extens = fileName.substring(fileName.lastIndexOf(".")+1);    //扩展名
        String uploadFileName = UUID.randomUUID()+extens;//FTP服务器保存的路径
        File fileDir = new File(path);
        if(!fileDir.exists()){  //如果不存在
            fileDir.setWritable(true);//打开修改文件的权限
            fileDir.mkdirs();   //创建文件
        }
        File tarageFile = new File(path,uploadFileName);
        Map map = Maps.newHashMap();
        try {
            file.transferTo(tarageFile);    //文件上传到服务器上
            FTPUtil.uploadFile(Lists.newArrayList(tarageFile));//上传到FTP服务器上
            tarageFile.delete();            //上传存在服务器上的文件
        }catch (IOException e){
            LOGGER.error("文件上传异常",e);
            return ServerResponse.createByError();
        }
        map.put("uri",tarageFile.getName());
        map.put("url", PropertiesUtil.getProperty("ftp.server.http.prefix")+tarageFile.getName());
        return ServerResponse.createBySuccess(map);
    }
}
