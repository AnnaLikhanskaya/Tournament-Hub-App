package ru.school.tournamenthub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с JWT токеном")
public record AuthResponse(
        @Schema(description = "JWT токен для доступа к API", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Тип токена", example = "Bearer")
        String type,

        @Schema(description = "Данные пользователя")
        UserResponse user
) {
    public AuthResponse(String token, UserResponse user) {
        this(token, "Bearer", user); // Конструктор по умолчанию с Bearer типом
    }
}