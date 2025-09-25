package ru.school.tournamenthub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.school.tournamenthub.model.enums.UserRole;

@Data
@Schema(description = "Запрос на создание/обновление пользователя")
public class UserRequest {

    @NotBlank(message = "Username обязателен")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    @Schema(description = "Имя пользователя", example = "coach_ivanov")
    private String username;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "coach@school.ru")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Schema(description = "Пароль", example = "securepassword")
    private String password;

    @Schema(description = "Роль пользователя", example = "COACH")
    private UserRole role;

    @NotBlank(message = "Полное имя обязательно")
    @Schema(description = "Полное имя", example = "Иванов Иван Иванович")
    private String fullName;
}