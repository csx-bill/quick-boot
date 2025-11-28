package com.quick.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.quick.boot.modules.*.mapper")
public class QuickBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickBootApplication.class, args);
	}

}
