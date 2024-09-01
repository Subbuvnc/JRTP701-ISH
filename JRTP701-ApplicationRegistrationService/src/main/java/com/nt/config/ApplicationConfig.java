package com.nt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {
	@Bean
	public RestTemplate createTemplate() {
		return  new RestTemplate();
	}
	
	@Bean(name="webclient")
	public WebClient createWenClient() {
		
		return WebClient.create();
	}

}
