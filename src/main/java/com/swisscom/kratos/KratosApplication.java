package com.swisscom.kratos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class KratosApplication {

    public static void main(String[] args) {
        SpringApplication.run(KratosApplication.class, args);
    }

}
