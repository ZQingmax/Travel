package com.mingde;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mingde.mapper")
public class WanderLensApplication {

    public static void main(String[] args) {
        SpringApplication.run(WanderLensApplication.class, args);
    }

}
