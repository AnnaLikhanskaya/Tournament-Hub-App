package ru.school.tournamenthub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.school.tournamenthub.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ с данными пользователя")
public record UserResponse(
        @Schema(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Имя пользователя", example = "ivanov_coach")
        String username,

        @Schema(description = "Email пользователя", example = "ivanov@school.ru")
        String email,

        @Schema(description = "Роль пользователя", example = "COACH")
        UserRole role,

        @Schema(description = "Полное имя пользователя", example = "Иванов Иван Иванович")
        String fullName,

        @Schema(description = "Дата создания учетной записи")
        LocalDateTime createdAt,

        @Schema(description = "Дата последнего обновления")
        LocalDateTime updatedAt,

        @Schema(description = "Статус активности учетной записи", example = "true")
        Boolean isActive,

        @Schema(description = "Версия для оптимистичной блокировки")
        Long version
) {}