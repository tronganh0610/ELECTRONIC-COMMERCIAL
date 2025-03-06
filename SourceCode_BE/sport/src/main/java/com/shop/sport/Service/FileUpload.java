package com.shop.sport.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface FileUpload {
    Map<String, String> uploadFile(MultipartFile multipartFile) throws IOException;

    Boolean  deleteFile(String pulic_id) throws IOException;

}