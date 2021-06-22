package com.swisscom.kratos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.*;
import com.swisscom.kratos.service.Device2ConfigServiceImpl;
import com.swisscom.kratos.service.MappingService;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.jupiter.api.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

		Collection<NetworkService> networkServices = mappingService.dryRun(device1Config);
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

		Collection<NetworkService> networkServices = mappingService.dryRun(device1Config);
		assertFalse(networkServices.isEmpty());
		assertEquals(1, networkServices.size());
		Iterator<NetworkService> iterator = networkServices.iterator();
		NetworkServiceB serviceB = (NetworkServiceB) iterator.next();
		assertEquals("id12", serviceB.getDevice());
		assertEquals("value12_2", serviceB.getConfiguration().get("configB2"));
		assertFalse(serviceB.getConfiguration().containsKey("configB1"));
	}

	@Test
	void mappingDevice1ToNetworkServiceB() {
		Device1Config device1Config = new Device1Config();
		device1Config.getConfig().put("param2", "tomato");

		Collection<NetworkService> networkServices = mappingService.dryRun(device1Config);
		assertFalse(networkServices.isEmpty());

		NetworkServiceB serviceB = (NetworkServiceB) networkServices.iterator().next();
		assertEquals("id11", serviceB.getDevice());
		assertEquals("value11", serviceB.getConfiguration().get("configB1"));
		assertEquals("value12", serviceB.getConfiguration().get("configB2"));

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
}
