package com.konkera.demoneo4j;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.konkera.demoneo4j.mapper"})
public class DemoNeo4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoNeo4jApplication.class, args);
    }

}
