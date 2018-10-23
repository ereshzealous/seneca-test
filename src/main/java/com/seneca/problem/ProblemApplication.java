package com.seneca.problem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.seneca.problem")
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class})

public class ProblemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProblemApplication.class, args);
	}
}
