package com.shop.sport.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUpload {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> uploadFile(MultipartFile multipartFile) throws IOException {

        String public_id = UUID.randomUUID().toString();


        String url_image = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "public_id", public_id,
                                "folder", "tronganh/products", // Specify the folder name
                                "transformation", new Transformation().width(320).height(320))
                        )
                .get("url")
                .toString();

        Map<String, String> tmp = new HashMap<>();
        tmp.put("public_id", public_id);
        tmp.put("url", url_image);

        return tmp;
    }

    @Override
    public Boolean deleteFile(String pulic_id) throws IOException {
        Map map = cloudinary.uploader()
                .destroy(pulic_id, Map.of("public_id", pulic_id));
        if (map.get("result").toString().equalsIgnoreCase("ok")) {
            return true;
        }
        return false;
    }
}