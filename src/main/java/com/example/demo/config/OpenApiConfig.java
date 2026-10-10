package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Service Center Management System")
                        .version("1.0")
                        .description(
                                "REST API for managing customers, vehicles, "
                                + "service bookings, mechanics, spare parts and invoices."
                        ));
    }
}