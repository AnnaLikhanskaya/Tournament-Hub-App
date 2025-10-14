package ru.school.tournamenthub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Запрос на аутентификацию")
public record AuthRequest(
        @NotBlank(message = "Username обязателен")
        @Schema(description = "Имя пользователя", example = "coach_ivanov")
        String username,

        @NotBlank(message = "Пароль обязателен")
        @Schema(description = "Пароль", example = "password123")
        String password
) {}
