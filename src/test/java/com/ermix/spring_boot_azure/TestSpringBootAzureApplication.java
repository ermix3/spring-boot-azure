package com.ermix.spring_boot_azure;

import org.springframework.boot.SpringApplication;

public class TestSpringBootAzureApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringBootAzureApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
