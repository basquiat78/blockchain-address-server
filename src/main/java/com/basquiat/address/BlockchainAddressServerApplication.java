package com.basquiat.address;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BlockchainAddressServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainAddressServerApplication.class, args);
	}
	
}
