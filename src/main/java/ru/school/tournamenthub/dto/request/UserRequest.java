package ru.school.tournamenthub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.school.tournamenthub.model.enums.UserRole;

@Schema(description = "Запрос на создание/обновление пользователя")
public record UserRequest(
        @NotBlank(message = "Username обязателен")
        @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
        @Schema(description = "Имя пользователя", example = "coach_ivanov")
        String username,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        @Schema(description = "Email пользователя", example = "coach@school.ru")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
        @Schema(description = "Пароль", example = "securepassword")
        String password,

        @Schema(description = "Роль пользователя", example = "COACH")
        UserRole role, // По умолчанию будет COACH если не указано

        @NotBlank(message = "Полное имя обязательно")
        @Schema(description = "Полное имя", example = "Иванов Иван Иванович")
        String fullName
) {}