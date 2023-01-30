package com.miranda.voting.assemblies.v1;

import com.miranda.voting.assemblies.v1.util.ValidateVote;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@SpringBootApplication
public class VotingAssembliesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingAssembliesApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
	}

	@Bean
	public RestTemplate restTemplate(){return new RestTemplate();}
}
