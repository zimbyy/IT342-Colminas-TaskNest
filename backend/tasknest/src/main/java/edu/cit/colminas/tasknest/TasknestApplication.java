package edu.cit.colminas.tasknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TasknestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasknestApplication.class, args);
	}

}
