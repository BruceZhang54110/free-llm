package com.free.agent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.free.agent.db.mapper")
@SpringBootApplication
public class FreeAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeAgentApplication.class, args);
    }

}
