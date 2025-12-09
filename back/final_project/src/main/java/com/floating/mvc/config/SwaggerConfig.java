package com.floating.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	
	
	 @Bean
	  public OpenAPI springBoardOpenAPI() {
		 final String securitySchemeName = "bearerAuth";
	      return new OpenAPI()
	              .info(new Info().title("SpringBoard API")
	              .description("Spring Boot Board REST API")
	              .version("v0.0.1")
	              .license(new License().name("SSAFY").url("https://www.ssafy.com")))
	              
	              .components(new Components()
	            		  .addSecuritySchemes(securitySchemeName, new SecurityScheme()
	            				  .name(securitySchemeName)
	            				  .type(SecurityScheme.Type.HTTP)
	            				  .scheme("bearer")
	            				  .bearerFormat("JWT")
	            		  )
	              )
	              .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
	  }
}
