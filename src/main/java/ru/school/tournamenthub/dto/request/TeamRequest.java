package ru.school.tournamenthub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.school.tournamenthub.model.enums.Gender;

@Schema(description = "Запрос на создание/обновление команды")
public record TeamRequest(
        @NotBlank(message = "Название команды обязательно")
        @Schema(
                description = "Название команды",
                example = "Зоркий",
                minLength = 1,
                maxLength = 100
        )
        String name,

        @NotBlank(message = "Город обязателен")
        @Schema(
                description = "Город, в котором базируется команда",
                example = "Красногорск",
                minLength = 1,
                maxLength = 50
        )
        String city,

        @NotNull(message = "Пол состава обязателен")
        @Schema(
                description = "Пол игроков в команде",
                example = "MALE",
                allowableValues = {"MALE", "FEMALE"}
        )
        Gender gender,

        // Это поле необязательное (нет аннотаций валидации)
        @Schema(
                description = "Возрастная группа игроков",
                example = "2011",
                maxLength = 10
        )
        String yearGroup
) {
}
