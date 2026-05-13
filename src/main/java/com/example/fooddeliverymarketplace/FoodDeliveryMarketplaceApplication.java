package com.example.fooddeliverymarketplace;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@OpenAPIDefinition(
        info = @Info(
                title = "Food Delivery Marketplace Application",
                version = "v1.0",
                description = "APIs for using Food Delivery Marketplace Application",
                contact = @Contact(
                        name = "Group Project",
                        url = "https://github.com/mirsaidmirjalilov"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://springdoc.org"),
                termsOfService = "https://swagger.io/terms/"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring 6 Wiki Documentation",
                url = "https://springshop.wiki.github.org/docs"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local server for development and testing"
                )
        }
)
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebSecurity
@EnableJpaRepositories
public class FoodDeliveryMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryMarketplaceApplication.class, args);
    }

}
