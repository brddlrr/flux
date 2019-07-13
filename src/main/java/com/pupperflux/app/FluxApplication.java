package com.pupperflux.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableReactiveMongoRepositories
public class FluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class, args);
    }

}
