package com;

import com.google.common.collect.Sets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

/**
 * Created by zoujiajian on 2017-3-9.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        Set<Object> resource = Sets.newHashSet();
        resource.add("classpath*:spring/applicationContext.xml");
        application.setSources(resource);
        application.run(args);
    }
}
