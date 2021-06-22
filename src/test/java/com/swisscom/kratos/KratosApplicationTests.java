package com.swisscom.kratos;

import com.swisscom.kratos.service.Device1ConfigServiceImpl;
import com.swisscom.kratos.service.Device2ConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KratosApplicationTests {

	@Autowired
	private Device1ConfigServiceImpl device1ConfigService;

	@Autowired
	private Device2ConfigServiceImpl device2ConfigService;

	@Test
	void contextLoads() {
	}

	@Test
	void deviceMode1lLoaded() {
		assertEquals("model1", device1ConfigService.serviceId());
	}


	@Test
	void deviceModel2Loaded() {
		assertEquals("model2", device2ConfigService.serviceId());
	}

}
