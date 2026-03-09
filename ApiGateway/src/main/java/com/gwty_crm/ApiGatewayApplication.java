package com.gwty_crm;

import com.gwty_crm.filter.AuthenticationFilter;
//import com.gwty_crm.security.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {
    @Bean
    public GlobalFilter authenticationGlobalFilter(AuthenticationFilter authFilter) {
        return (exchange, chain) ->
                authFilter.filter(exchange, webFilterChain -> chain.filter(exchange));
    }

    public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
