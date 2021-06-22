package com.swisscom.kratos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
@ComponentScan
public class KratosApplication {

    @Bean
    @Primary
    public ObjectMapper defaultMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectMapper yamlObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    public static void main(String[] args) {
        SpringApplication.run(KratosApplication.class, args);
    }

}
