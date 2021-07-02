package com.swisscom.kratos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.Device1Config;
import com.swisscom.kratos.model.Device2Config;
import com.swisscom.kratos.model.NetworkService;
import com.swisscom.kratos.model.NetworkServiceA;
import com.swisscom.kratos.model.NetworkServiceB;
import com.swisscom.kratos.model.NetworkServiceC;
import com.swisscom.kratos.service.MappingService;
import com.swisscom.kratos.service.MappingServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KratosApplicationTests {

    @Autowired
    private MappingService mappingService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void mappingDevice1ToMultipleServices() {
        Device1Config device1Config = config1("device11.json");

        Collection<NetworkService> networkServices = mappingService
                .dryRun(device1Config, mappingService.getMappingLogic());
        assertFalse(networkServices.isEmpty());
        assertEquals(2, networkServices.size());
        Iterator<NetworkService> iterator = networkServices.iterator();
        NetworkServiceA serviceA = (NetworkServiceA) iterator.next();
        NetworkServiceB serviceB = (NetworkServiceB) iterator.next();
        assertEquals("id11", serviceA.getDevice());
        assertEquals("value11_1", serviceA.getConfiguration().get("paramA1"));
        assertEquals("id11", serviceB.getDevice());
        assertEquals("value11_2", serviceB.getConfiguration().get("configB2"));
        assertEquals("value11_1", serviceB.getConfiguration().get("configB1"));
    }

    @Test
    void mappingDevice1ToService() {
        Device1Config device1Config = config1("device12.json");

        Collection<NetworkService> networkServices = mappingService
                .dryRun(device1Config, mappingService.getMappingLogic());
        assertFalse(networkServices.isEmpty());
        assertEquals(1, networkServices.size());
        Iterator<NetworkService> iterator = networkServices.iterator();
        NetworkServiceB serviceB = (NetworkServiceB) iterator.next();
        assertEquals("id12", serviceB.getDevice());
        assertEquals("value12_2", serviceB.getConfiguration().get("configB2"));
        assertEquals("", serviceB.getConfiguration().get("configB1"));
    }

    @Test
    void mappingDevice2ToService() {
        Device2Config device2Config = config2("device21.txt");

        Collection<NetworkService> networkServices = mappingService
                .dryRun(device2Config, mappingService.getMappingLogic());
        assertFalse(networkServices.isEmpty());

        assertEquals(1, networkServices.size());
        Iterator<NetworkService> iterator = networkServices.iterator();
        NetworkServiceA serviceA = (NetworkServiceA) iterator.next();
        assertEquals("uuid21", serviceA.getDevice());
        assertEquals("value21_1", serviceA.getConfiguration().get("paramA1"));
        assertEquals("0", serviceA.getConfiguration().get("paramA2"));
    }

    @Test
    void mappingDevice2ToServiceAC() {
        Device2Config device2Config = config2("device22.txt");

        Collection<NetworkService> networkServices = mappingService
                .dryRun(device2Config, mappingService.getMappingLogic());
        assertFalse(networkServices.isEmpty());

        assertEquals(2, networkServices.size());
        Iterator<NetworkService> iterator = networkServices.iterator();
        NetworkServiceA serviceA = (NetworkServiceA) iterator.next();
        assertEquals("uuid22", serviceA.getDevice());
        assertEquals("value22_1", serviceA.getConfiguration().get("paramA1"));
        assertEquals("0", serviceA.getConfiguration().get("paramA2"));

        NetworkServiceC serviceC = (NetworkServiceC) iterator.next();
        assertEquals("uuid22", serviceC.getDevice());
        assertEquals("value22_3", serviceC.getConfiguration().get("configC2"));
        assertEquals("value22_1", serviceC.getConfiguration().get("configC1"));
    }

    @Test
    void mappingDevice2ToServiceB() {
        Device2Config device2Config = config2("device23.txt");

        Collection<NetworkService> networkServices = mappingService
                .dryRun(device2Config, mappingService.getMappingLogic());
        assertFalse(networkServices.isEmpty());

        assertEquals(1, networkServices.size());
        Iterator<NetworkService> iterator = networkServices.iterator();
        NetworkServiceB serviceB = (NetworkServiceB) iterator.next();
        assertEquals("uuid23", serviceB.getDevice());
        assertEquals("value23_2", serviceB.getConfiguration().get("configB2"));
        assertEquals("value23_1", serviceB.getConfiguration().get("configB1"));
    }

    @Value("${services.output}")
    private String outputDir;

    @Test
    void testScheduling() throws IOException {
        MappingServiceImpl mappingImpl = (MappingServiceImpl) mappingService;
        File dir = new File(outputDir);
        FileUtils.cleanDirectory(dir);
        mappingImpl.createExecutionJob().run();
        assertNotEquals(0, dir.listFiles().length);
    }

    private Device1Config config1(String file) {
        try {
            Device1Config config = new Device1Config();
            HashMap map = objectMapper.readValue(new File("input", file), HashMap.class);
            config.setConfig(map);
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Device2Config config2(String file) {
        try (FileInputStream stream = new FileInputStream(new File("input", file))) {
            Properties appProps = new Properties();
            appProps.load(stream);
            Map<String, Object> config = new HashMap<>();
            appProps.forEach((key, value) -> config.put(key + "", value));
            Device2Config device2Config = new Device2Config();
            device2Config.setConfig(config);
            return device2Config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
