package com.swisscom.kratos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.NetworkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Persist service updates in files
 */
@Service
@Slf4j
public class NetworkServiceConfigurerImpl implements NetworkServiceConfigurer {

    private final String outputDir;

    private final ObjectMapper objectMapper;

    public NetworkServiceConfigurerImpl(@Value("${services.output}") String outputDir,
                                        @Autowired
                                        @Qualifier("yamlObjectMapper") ObjectMapper objectMapper) {

        this.outputDir = outputDir;
        this.objectMapper = objectMapper;
    }

    @Override
    public void apply(NetworkService networkService) {
        String fileName = networkService.getServiceId();
        if (!fileName.endsWith(".yaml")) {
            fileName = fileName + ".yaml";
        }
        log.info("Apply configuration for service {}", networkService);
        try {
            objectMapper.writeValue(new File(outputDir, fileName), networkService);
        } catch (IOException e) {
            log.error("Failed to persist network service {}", networkService, e);
            e.printStackTrace();
        }
    }
}
