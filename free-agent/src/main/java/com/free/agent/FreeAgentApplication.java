package com.free.agent;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@MapperScan("com.free.agent.db.mapper")
@SpringBootApplication
public class FreeAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeAgentApplication.class, args);
    }

}
