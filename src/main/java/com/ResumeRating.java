package com;

import com.tasks.TasksExecutingThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ResumeRating extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ResumeRating.class);
        app.run();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ResumeRating.class);
    }

    @Override
    public void run(String...args) throws Exception {
        TasksExecutingThread thread = applicationContext.getBean(TasksExecutingThread.class);
       // thread.teach();
        thread.run();
    }
}
