package ru.school.tournamenthub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                        .description("""
                                 Backend API для управления спортивными турнирами.
                                                                \s
                                 ### Система прав доступа:
                                 - **ADMIN**: полный доступ ко всем ресурсам
                                 - **COACH**: доступ только к своим командам и турнирам
                                                                \s
                              Для аутентификации используйте Bearer токен в заголовке Authorization.
                                """))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}