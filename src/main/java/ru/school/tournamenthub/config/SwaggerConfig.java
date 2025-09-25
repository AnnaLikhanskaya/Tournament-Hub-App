package ru.school.tournamenthub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tournament Hub+ API")
                        .version("1.0.0")
                        .description("Backend API для управления спортивными турнирами")
                        .contact(new Contact()
                                .name("Tournament Hub+ Team")
                                .email("support@tournamenthub.com")));
    }
}
