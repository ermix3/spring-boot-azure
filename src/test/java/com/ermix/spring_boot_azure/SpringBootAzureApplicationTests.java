package com.ermix.spring_boot_azure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringBootAzureApplicationTests {

	@Test
	void contextLoads() {
	}

}
