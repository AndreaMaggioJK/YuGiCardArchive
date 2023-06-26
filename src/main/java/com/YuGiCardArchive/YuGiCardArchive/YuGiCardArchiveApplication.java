package com.YuGiCardArchive.YuGiCardArchive;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class YuGiCardArchiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuGiCardArchiveApplication.class, args);
	}


	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("http://localhost:4200");
		corsConfiguration.addAllowedHeader("Origin");
		corsConfiguration.addAllowedHeader("Access-Control-Allow-Origin");
		corsConfiguration.addAllowedHeader("Content-Type");
		corsConfiguration.addAllowedHeader("Accept");
		corsConfiguration.addAllowedHeader("Authorization");
		corsConfiguration.addAllowedHeader("X-Requested-With");
		corsConfiguration.addAllowedHeader("Access-Control-Request-Method");
		corsConfiguration.addAllowedHeader("Access-Control-Request-Headers");
		corsConfiguration.addAllowedHeader("Access-Control-Allow-Methods");
		corsConfiguration.addAllowedHeader("Access-Control-Allow-Headers");
		corsConfiguration.addExposedHeader("Content-Type");
		corsConfiguration.addExposedHeader("Accept");
		corsConfiguration.addExposedHeader("Authorization");
		corsConfiguration.addExposedHeader("Access-Control-Allow-Origin");
		corsConfiguration.addExposedHeader("Access-Control-Allow-Credentials");
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
