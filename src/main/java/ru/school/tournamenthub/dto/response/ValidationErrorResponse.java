package ru.school.tournamenthub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Ответ с информацией об ошибках валидации")
@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {

    @Schema(description = "Карта ошибок валидации (поле -> сообщение об ошибке)")
    private Map<String, String> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, String message, Map<String, String> errors) {
        super(timestamp, status, error, message, "");
        this.errors = errors;
    }
}