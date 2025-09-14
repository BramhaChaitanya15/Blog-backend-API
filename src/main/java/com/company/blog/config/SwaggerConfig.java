package com.company.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI customOpenAPI() {
		// Info and contact
		Contact contact = new Contact().name("Bramha Chaitanya").email("bramhachaitanya15@gmail.com");

		Info info = new Info().title("Blog API : Backend").version("1.0")
				.description("API documentation for the Blog project").contact(contact);

		// Security scheme for JWT
		SecurityScheme securityScheme = new SecurityScheme().name("Authorization") // Header name
				.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

		// Security requirement
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		// OpenAPI configuration
		return new OpenAPI().info(info).addSecurityItem(securityRequirement) // Apply globally
				.components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
	}

}
