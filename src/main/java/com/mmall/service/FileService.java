package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ServerResponse fileUp(MultipartFile file, String path);
}
