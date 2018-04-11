package com.jsut.zachweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@ComponentScan(basePackages = {"com.jsut"})
@MapperScan("com.jsut.zachweb.dao")
public class ZachwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZachwebApplication.class, args);
	}
}
