package com.cboard.owlswap.owlswap_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OwlSwapBackendApplication
{

	public static void main(String[] args)
	{

		SpringApplication.run(OwlSwapBackendApplication.class, args);

	}

}