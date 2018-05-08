package com.jsut.zachweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@ComponentScan(basePackages = {"com.jsut"})
@MapperScan("com.jsut.zachweb.dao")
public class ZachwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZachwebApplication.class, args);
	}
	/*//设置ajax跨域请求
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurerAdapter(){

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//设置上传文件大小限制
		factory.setMaxFileSize("10MB");
		//设置上传总数据大小
		factory.setMaxRequestSize("15MB");
		return factory.createMultipartConfig();
	}*/
}
