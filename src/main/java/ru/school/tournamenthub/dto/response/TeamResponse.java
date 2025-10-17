package ru.school.tournamenthub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.school.tournamenthub.model.enums.Gender;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ с данными команды")
public record TeamResponse(

        @Schema(description = "ID команды", example = "1")
        UUID id,

        @Schema(description = "Название команды", example = "Зоркий")
        String name,

        @Schema(description = "Город", example = "Красногорск")
        String city,

        @Schema(description = "Пол состава", example = "MALE")
        Gender gender,

        @Schema(description = "Возрастная группа", example = "2011")
        String yearGroup,

        @Schema(description = "Отображаемое имя", example = "Юноши, Зоркий 2011, Красногорск")
        String fullDisplayName,

        @Schema(description = "ID тренера")
        UUID coachId,

        @Schema(description = "Имя тренера", example = "Иванов Иван Иванович")
        String coachName,

        @Schema(description = "Дата создания")
        LocalDateTime createdAt
) {
}