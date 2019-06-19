package com.tophousekeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.tophousekeeper.dao.function")
public class TopHousekeeperApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TopHousekeeperApplication.class, args);
	}

	protected SpringApplicationBuilder configue(SpringApplicationBuilder application){
		return application.sources(TopHousekeeperApplication.class);
	}
}
