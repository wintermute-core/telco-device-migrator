package com.swisscom.kratos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.swisscom.kratos.model.NetworkServiceA;
import com.swisscom.kratos.model.NetworkServiceB;
import com.swisscom.kratos.model.NetworkServiceC;
import com.swisscom.kratos.service.NetworkServiceConfigurer;
import com.swisscom.kratos.service.NetworkServiceConfigurerImpl;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class NetworkServiceConfigurerTest {

    private final File output = new File("app-output");
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private NetworkServiceConfigurer networkServiceConfigurer;


    @BeforeEach
    void init() {
        output.mkdirs();
        networkServiceConfigurer = new NetworkServiceConfigurerImpl(output.getName(), mapper);
    }

    @Test
    void networkServiceAPersistence() throws IOException {
        NetworkServiceA networkServiceA = new NetworkServiceA();
        networkServiceA.setServiceId("network-service-a-" + System.currentTimeMillis());
        networkServiceA.setDevice("potato");
        networkServiceA.setConfiguration(Map.of("tomato", "carrot"));
        networkServiceConfigurer.apply(networkServiceA);

        File file = new File(output, networkServiceA.getServiceId() + ".yaml");
        assertTrue(file.exists());
        assertNotEquals(0, file.length());

        NetworkServiceA readInstance = mapper.readValue(file, NetworkServiceA.class);
        assertEquals(networkServiceA.getDevice(), readInstance.getDevice());
    }

    @Test
    void networkServiceBPersistence() throws IOException {
        NetworkServiceB networkServiceB = new NetworkServiceB();
        networkServiceB.setServiceId("network-service-b-" + System.currentTimeMillis());
        networkServiceB.setDevice("potato");
        networkServiceB.setConfiguration(Map.of("tomato", "carrot"));
        networkServiceConfigurer.apply(networkServiceB);

        File file = new File(output, networkServiceB.getServiceId() + ".yaml");
        assertTrue(file.exists());
        assertNotEquals(0, file.length());

        NetworkServiceB readInstance = mapper.readValue(file, NetworkServiceB.class);
        assertEquals(networkServiceB.getDevice(), readInstance.getDevice());
    }

    @Test
    void networkServiceCPersistence() throws IOException {
        NetworkServiceC networkServiceC = new NetworkServiceC();
        networkServiceC.setServiceId("network-service-c-" + System.currentTimeMillis());
        networkServiceC.setDevice("potato");
        networkServiceC.setConfiguration(Map.of("tomato", "carrot"));
        networkServiceConfigurer.apply(networkServiceC);

        File file = new File(output, networkServiceC.getServiceId() + ".yaml");
        assertTrue(file.exists());
        assertNotEquals(0, file.length());

        NetworkServiceC readInstance = mapper.readValue(file, NetworkServiceC.class);
        assertEquals(networkServiceC.getDevice(), readInstance.getDevice());
    }
}
