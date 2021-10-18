package com.kn.wallets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WalletsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletsApplication.class, args);
	}

}
