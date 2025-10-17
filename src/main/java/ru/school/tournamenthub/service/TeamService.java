package ru.school.tournamenthub.service;

import org.springframework.transaction.annotation.Transactional;
import ru.school.tournamenthub.dto.request.TeamRequest;
import ru.school.tournamenthub.dto.response.TeamResponse;

import java.util.List;
import java.util.UUID;

public interface TeamService {

    // Метод для создания новой команды
    // Принимает данные запроса и ID тренера который создает команду
    TeamResponse createTeam(TeamRequest teamRequest, UUID coachId);

    // Метод для получения команды по ID
    // Возвращает TeamResponse или бросает исключение если команда не найдена
    @Transactional(readOnly = true)
    TeamResponse getTeamById(UUID id);

    // Метод для получения всех команд в системе
    // Возвращает список всех команд преобразованных в TeamResponse
    @Transactional(readOnly = true)
    List<TeamResponse> getAllTeams();

    // Метод для получения команд конкретного тренера
    // Используется для страницы "Мои команды"
    @Transactional(readOnly = true)
    List<TeamResponse> getTeamsByCoach(UUID coachId);

    // Метод для обновления команды
    // Проверяет права доступа - может ли пользователь редактировать эту команду
    TeamResponse updateTeam(UUID id, TeamRequest teamRequest, UUID currentUserId);

    // Метод для удаления команды
    // Проверяет права доступа перед удалением
    void deleteTeam(UUID id, UUID currentUserId);

    // Метод для проверки является ли пользователь владельцем команды
    // Используется для проверки прав доступа
    @Transactional(readOnly = true)
    boolean isTeamOwner(UUID teamId, UUID userId);
}