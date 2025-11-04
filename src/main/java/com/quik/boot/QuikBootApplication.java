package com.quik.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.quik.boot.modules.*.mapper")
public class QuikBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuikBootApplication.class, args);
	}

}
