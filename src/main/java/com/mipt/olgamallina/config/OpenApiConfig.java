package com.mipt.olgamallina.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do List Manager API")
                        .description("Homework #3 Persistence Layer API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("olgamallina")
                                .email("olgamallina@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project documentation")
                        .url("https://example.com/docs"));
    }
}