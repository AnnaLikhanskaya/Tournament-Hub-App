package ru.school.tournamenthub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Разрешить доступ к Swagger без аутентификации
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api-docs/**"
                        ).permitAll()
                        // Остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()) // Отключить форму логина
                .httpBasic(httpBasic -> httpBasic.disable()); // Отключить basic auth

        return http.build();
    }
}