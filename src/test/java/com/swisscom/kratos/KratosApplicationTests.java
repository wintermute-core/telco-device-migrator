package com.swisscom.kratos;

import com.swisscom.kratos.model.Device1Config;
import com.swisscom.kratos.model.Device2Config;
import com.swisscom.kratos.model.NetworkService;
import com.swisscom.kratos.model.NetworkServiceA;
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

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class KratosApplicationTests {

	@Autowired
	private MappingService mappingService;

	@Test
	void contextLoads() {
	}

	@Test
	void mappingDevice1ToNetworkServiceA() {
		Device1Config device1Config = new Device1Config();
		device1Config.getConfig().put("param1", "potato");

		Optional<NetworkService> networkService = mappingService.dryRun(device1Config);
		assertTrue(networkService.isPresent());
		NetworkServiceA serviceA = (NetworkServiceA) networkService.get();
		assertEquals("id11", serviceA.getDevice());
		assertEquals("value11", serviceA.getConfiguration().get("paramA1"));
	}

	@Test
	void mappingDevice2() {
		Device2Config device2Сonfig = new Device2Config();
		mappingService.dryRun(device2Сonfig);
	}
}
