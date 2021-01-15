package com.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Controller
@RequestMapping("/fonts")
public class FontsController {

    @RequestMapping(value = "/materialdesignicons-webfont.woff", method = RequestMethod.GET)
    public ResponseEntity css(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/css");

        try {
            InputStream is = new FileInputStream(Paths.get("/src/main/resources/fonts/materialdesignicons-webfont.woff").toString());
            IOUtils.copy(is, response.getOutputStream());
            IOUtils.closeQuietly(is);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/materialdesignicons-webfont.woff2", method = RequestMethod.GET)
    public ResponseEntity css2(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/css");

        try {
            InputStream is = new FileInputStream(Paths.get("/src/main/resources/fonts/materialdesignicons-webfont.woff2").toString());
            IOUtils.copy(is, response.getOutputStream());
            IOUtils.closeQuietly(is);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/materialdesignicons-webfont.ttf", method = RequestMethod.GET)
    public ResponseEntity ttf(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/css");

        try {
            InputStream is = new FileInputStream(Paths.get("/src/main/resources/fonts/materialdesignicons-webfont.ttf").toString());
            IOUtils.copy(is, response.getOutputStream());
            IOUtils.closeQuietly(is);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
