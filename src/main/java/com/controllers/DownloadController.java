package com.controllers;

import org.apache.poi.util.IOUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.print.DocFlavor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @GetMapping("/view")
    public ModelAndView downloadFilePage(@RequestParam("file_url") String fileUrl) {
        ModelAndView viewFileModelAndView = new ModelAndView("view_files");
        viewFileModelAndView.addObject("fileSrc", fileUrl);
        return viewFileModelAndView;
    }

    @GetMapping("/resume")
    @ResponseBody
    public HttpEntity<byte[]> downloadResume(@RequestParam("file_path") String filePath, @RequestParam("file_name") String fileName) {
        byte[] resumeContent = new byte[0];

        try {
            FileInputStream stream = new FileInputStream(filePath);
            resumeContent = IOUtils.toByteArray(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileName = fileName.replace(" ", "_");
        String fileType = filePath.replace(".", "@").split("@")[filePath.replace(".", "@").split("@").length - 1];
        HttpHeaders header = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        header.setContentDisposition(contentDisposition);
        header.setContentLength(resumeContent.length);

        switch (fileType.toLowerCase()){
            case "pdf":
                header.setContentType(MediaType.APPLICATION_PDF);
                break;
            case "docx":
                header.setContentType(new MediaType("application", "avnd.openxmlformats-officedocument.wordprocessingml.document"));
                break;
            default:
                break;
        }


        return new HttpEntity<>(resumeContent, header);
    }
}
