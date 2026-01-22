package com.ingenieraglobal.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = "com.ingenieraglobal")
@EntityScan(basePackages = "com.ingenieraglobal.models")
@EnableJpaRepositories(basePackages = "com.ingenieraglobal.repositories")
@ComponentScan(basePackages = "com.ingenieraglobal")
public class IngenieraGlobalECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngenieraGlobalECommerceApplication.class, args);
	}

}
