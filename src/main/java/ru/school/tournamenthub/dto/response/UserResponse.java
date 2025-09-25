package ru.school.tournamenthub.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.school.tournamenthub.model.enums.UserRole;

import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с данными пользователя")
public class UserResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivanov_coach")
    private String username;

    @Schema(description = "Email пользователя", example = "ivanov@school.ru")
    private String email;

    @Schema(description = "Роль пользователя", example = "COACH")
    private UserRole role;

    @Schema(description = "Полное имя пользователя", example = "Иванов Иван Иванович")
    private String fullName;

    // Дата и время создания записи в базе данных
    // Автоматически устанавливается при создании
    @Schema(description = "Дата создания учетной записи")
    private LocalDateTime createdAt;

    // Дата и время последнего обновления записи
    // Автоматически обновляется при изменении данных
    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;

    // false - пользователь заблокирован и не может войти в систему
    @Schema(description = "Статус активности учетной записи", example = "true")
    private Boolean isActive;

    /*
    Пароль отсутсвует из за безопасности, тк не должен отправляться клиенту
     */
}
