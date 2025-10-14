package ru.school.tournamenthub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.school.tournamenthub.dto.request.AuthRequest;
import ru.school.tournamenthub.dto.request.UserRequest;
import ru.school.tournamenthub.dto.response.AuthResponse;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.service.AuthService;
import ru.school.tournamenthub.service.UserService;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для входа в систему и регистрации")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя и получение JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "403", description = "Учетная запись неактивна")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Запрос на вход от пользователя: {}", authRequest.username());

        AuthResponse authResponse = authService.authenticate(authRequest);
        log.info("Пользователь {} успешно вошел в систему", authRequest.username());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя", description = "Создание новой учетной записи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest userRequest) {
        log.info("Запрос на регистрацию пользователя: {}", userRequest.username());

        UserResponse userResponse = userService.createUser(userRequest);
        AuthRequest authRequest = new AuthRequest(userRequest.username(), userRequest.password());
        AuthResponse authResponse = authService.authenticate(authRequest);

        log.info("Пользователь {} успешно зарегистрирован", userRequest.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход из системы", description = "Завершение сессии пользователя")
    @ApiResponse(responseCode = "204", description = "Успешный выход")
    public ResponseEntity<Void> logout() {
        log.info("Запрос на выход из системы");

        authService.logout();

        log.info("Пользователь вышел из системы");
        return ResponseEntity.noContent().build();
    }
}