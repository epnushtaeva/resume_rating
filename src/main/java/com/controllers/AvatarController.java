package com.controllers;

import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/avatar")
public class AvatarController {

    @GetMapping("/get")
    @ResponseBody
    public HttpEntity<byte[]> downloadAvatar(@RequestParam("file_path") String filePath) {
        if(StringUtils.isEmpty(filePath) || filePath.equals("undefined") || filePath.equals("null")){
            return null;
        }

        String fileType = filePath.replace(".", "@").split("@")[filePath.replace(".", "@").split("@").length - 1];

        if(Arrays.asList("jpg","jpeg","png","bmp").contains(fileType.toLowerCase())) {
            byte[] avatarContent = new byte[0];

            try {
                FileInputStream stream = new FileInputStream(filePath);
                avatarContent = IOUtils.toByteArray(stream);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image."+fileType);
            header.setContentLength(avatarContent.length);

            switch (fileType.toLowerCase()){
                case "png":
                    header.setContentType(MediaType.IMAGE_PNG);
                    break;
                case "jpg":
                    header.setContentType(MediaType.IMAGE_JPEG);
                    break;
                case "jpeg":
                    header.setContentType(MediaType.IMAGE_JPEG);
                    break;
                default:
                    break;
            }

            return new HttpEntity<>(avatarContent, header);
        } else {
            return null;
        }
    }
}
