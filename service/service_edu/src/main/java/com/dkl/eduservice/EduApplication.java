package com.dkl.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: chddald
 * @version: v1.0
 * @Date: 2021/3/29 16:34
 */

@SpringBootApplication
@ComponentScan(basePackages = {"com.dkl"})
public class EduApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
