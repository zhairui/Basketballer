package com.jerry.BasketBaller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.jerry.BasketBaller.mapper"})
public class BasketBallerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasketBallerApplication.class, args);
	}

}
