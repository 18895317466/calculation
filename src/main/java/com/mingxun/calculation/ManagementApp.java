package com.mingxun.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author wangpeng
 * @date 2018/10/12
 */
@SpringBootApplication
@EnableAutoConfiguration
public class ManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApp.class, args);
    }

}
