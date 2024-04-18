package com.jokes.jokes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class JokesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokesApplication.class, args);
	}

}
