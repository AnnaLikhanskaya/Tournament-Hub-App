package ru.school.tournamenthub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.school.tournamenthub.dto.request.UserRequest;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями системы")
public class UserController {

    private final UserService userService;

    /**
     * Создание нового пользователя
     * POST /api/users
     */
    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя (тренера или администратора)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким username или email уже существует")
    })
    public ResponseEntity<UserResponse> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class), examples = {
                            @ExampleObject(name = "Тренер", value = """
                                    {
                                      "username": "coach_ivanov",
                                      "email": "ivanov@sportschool.ru",
                                      "password": "password123",
                                      "role": "COACH",
                                      "fullName": "Иванов Иван Иванович"
                                    }"""),
                            @ExampleObject(name = "Администратор", value = """
                                    {
                                      "username": "admin",
                                      "email": "admin@sportschool.ru",
                                      "password": "admin123",
                                      "role": "ADMIN", 
                                      "fullName": "Администратор Системы"
                                    }""")
                    })
            )
            @Valid @RequestBody UserRequest userRequest) {

        log.info("Получен запрос на создание пользователя: {}", userRequest.getUsername());

        UserResponse createdUser = userService.createUser(userRequest);

        log.info("Пользователь создан успешно: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    /**
     * Получение пользователя по ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        log.info("Получен запрос на получение пользователя с ID: {}", id);

        UserResponse user = userService.getUserById(id);

        log.info("Пользователь с ID {} найден", id);
        return ResponseEntity.ok(user);
    }

    /**
     * Получение всех пользователей
     * GET /api/users
     */
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей системы")
    @ApiResponse(responseCode = "200", description = "Список пользователей получен успешно")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");

        List<UserResponse> users = userService.getAllUsers();

        log.info("Найдено {} пользователей", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * Обновление пользователя
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Username или email уже занят")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Получен запрос на обновление пользователя с ID: {}", id);

        UserResponse updatedUser = userService.updateUser(id, userRequest);

        log.info("Пользователь с ID {} успешно обновлен", id);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Удаление пользователя (деактивация)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Деактивирует пользователя (мягкое удаление)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно деактивирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        log.info("Получен запрос на деактивацию пользователя с ID: {}", id);

        userService.deleteUser(id);

        log.info("Пользователь с ID {} деактивирован", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Активация пользователя
     * PATCH /api/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Активировать пользователя", description = "Активирует ранее деактивированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь активирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        log.info("Получен запрос на активацию пользователя с ID: {}", id);

        userService.activateUser(id);

        log.info("Пользователь с ID {} активирован", id);
        return ResponseEntity.ok().build();
    }
}