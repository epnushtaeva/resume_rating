package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvc extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations(
                "classpath:/js/");
        registry.addResourceHandler("/css/**").addResourceLocations(
                "classpath:/css/");
        registry.addResourceHandler("/img/**").addResourceLocations(
                "classpath:/img/");
    }
}
