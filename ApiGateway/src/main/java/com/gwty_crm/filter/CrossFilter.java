package com.gwty_crm.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CrossFilter {

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration cors = new CorsConfiguration();

		// ✅ Allow your React app
		cors.addAllowedOriginPattern("http://localhost:3000");

		cors.addAllowedHeader("*");
		cors.addAllowedMethod("*");
		cors.setAllowCredentials(true);
		cors.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);

		return new CorsWebFilter(source);
	}
}