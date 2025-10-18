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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.school.tournamenthub.dto.request.TeamRequest;
import ru.school.tournamenthub.dto.response.TeamResponse;
import ru.school.tournamenthub.service.TeamService;
import ru.school.tournamenthub.service.UserService;
import ru.school.tournamenthub.service.security.SecurityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Команды", description = "API для управления спортивными командами")
public class TeamController {
    private final TeamService teamService;
    private final SecurityService securityService;
    private final UserService userService;

    @PostMapping
//    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    @Operation(summary = "Создать новую команду",
            description = "Создает новую команду. Тренеры могут создавать только свои команды.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Команда успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные команды"),
            @ApiResponse(responseCode = "409", description = "Команда с такими параметрами уже существует")
    })
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request) {
        log.info("Запрос на создание команды: {}", request.name());

        UUID coachId = securityService.getCurrentUserId();

        TeamResponse createdTeam = teamService.createTeam(request, coachId);
        log.info("Команда создана с ID: {}", createdTeam.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить команду по ID", description = "Возвращает данные команды по её идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда найдена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable UUID id) {
        log.info("Запрос на получение команды с ID: {}", id);

        TeamResponse team = teamService.getTeamById(id);
        log.info("Команда с ID {} найдена", id);

        return ResponseEntity.ok(team);
    }

    @GetMapping
    @Operation(summary = "Получить все команды", description = "Возвращает список всех команд системы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список команд получен успешно"),
            @ApiResponse(responseCode = "204", description = "Команды не найдены")
    })
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        log.info("Запрос на получение всех команд");

        List<TeamResponse> teams = teamService.getAllTeams();

        if (teams.isEmpty()) {
            log.info("Команды не найдены");
            return ResponseEntity.noContent().build();
        }

        log.info("Найдено {} команд", teams.size());
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('COACH')")
    @Operation(summary = "Получить мои команды", description = "Возвращает список команд текущего аутентифицированного тренера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список команд получен успешно"),
            @ApiResponse(responseCode = "204", description = "У вас пока нет команд")
    })
    public ResponseEntity<List<TeamResponse>> getMyTeams() {
        log.info("Запрос на получение команд текущего пользователя");

        UUID currentUserId = securityService.getCurrentUserId();
        List<TeamResponse> myTeams = teamService.getTeamsByCoach(currentUserId);

        if (myTeams.isEmpty()) {
            log.info("У пользователя {} нет команд", currentUserId);
            return ResponseEntity.noContent().build();
        }

        log.info("Найдено {} команд пользователя {}", myTeams.size(), currentUserId);
        return ResponseEntity.ok(myTeams);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    @Operation(summary = "Обновить команду", description = "Обновляет данные существующей команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для редактирования команды"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable UUID id,
            @Valid @RequestBody TeamRequest request) {

        log.info("Запрос на обновление команды с ID: {}", id);

        UUID currentUserId = securityService.getCurrentUserId();
        TeamResponse updatedTeam = teamService.updateTeam(id, request, currentUserId);

        log.info("Команда с ID {} успешно обновлена", id);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    @Operation(summary = "Удалить команду", description = "Удаляет команду по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Команда успешно удалена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления команды"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        log.info("Запрос на удаление команды с ID: {}", id);

        UUID currentUserId = securityService.getCurrentUserId();
        teamService.deleteTeam(id, currentUserId);
        log.info("Команда с ID {} успешно удалена", id);
        return ResponseEntity.noContent().build();
    }
}