package ru.school.tournamenthub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.tournamenthub.dto.request.TeamRequest;
import ru.school.tournamenthub.dto.response.TeamResponse;
import ru.school.tournamenthub.exception.UserNotFoundException;
import ru.school.tournamenthub.model.entity.Team;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.repository.TeamRepository;
import ru.school.tournamenthub.repository.UserRepository;
import ru.school.tournamenthub.service.TeamService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public TeamResponse createTeam(TeamRequest teamRequest, UUID coachId) {
        log.info("Создание команды: {} для тренера {}", teamRequest.name(), coachId);

        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new UserNotFoundException("Тренер не найден: " + coachId));

        // Проверка на дубликат перед созданием
        if (teamRepository.existsByNameAndCityAndGenderAndYearGroupAndCoachId(
                teamRequest.name(),
                teamRequest.city(),
                teamRequest.gender(),
                teamRequest.yearGroup(),
                coachId)) {
            throw new IllegalArgumentException("Команда с такими параметрами уже существует");
        }

        Team team = Team.builder()
                .name(teamRequest.name())
                .city(teamRequest.city())
                .gender(teamRequest.gender())
                .yearGroup(teamRequest.yearGroup())
                .coach(coach)
                .owner(coach)
                .build();

        Team savedTeam = teamRepository.save(team);
        log.info("Команда создана с ID: {}", savedTeam.getId());
        return convertToResponse(savedTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(UUID id) {
        log.info("Получение команды с ID: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Команда не найдена: " + id));
        return convertToResponse(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {
        log.info("Получение всех команд");
        List<Team> teams = teamRepository.findAll();
        log.info("Найдено {} команд", teams.size());
        return teams.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamsByCoach(UUID coachId) {
        log.info("Получение команд тренера: {}", coachId);
        List<Team> teams = teamRepository.findByCoachId(coachId);
        log.info("Найдено {} команд для тренера {}", teams.size(), coachId);
        return teams.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public TeamResponse updateTeam(UUID id, TeamRequest teamRequest, UUID currentUserId) {
        log.info("Обновление команды с ID: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Команда не найдена: " + id));

        // Проверка прав
        if (!isTeamOwner(id, currentUserId)) {
            throw new SecurityException("Недостаточно прав для редактирования команды");
        }

        // Проверка на дубликат (исключая текущую команду)
        if (teamRepository.existsByNameAndCityAndGenderAndYearGroupAndCoachIdAndIdNot(
                teamRequest.name(),
                teamRequest.city(),
                teamRequest.gender(),
                teamRequest.yearGroup(),
                currentUserId,
                id)) {
            throw new IllegalArgumentException("Команда с такими параметрами уже существует");
        }

        // Обновление полей
        team.setName(teamRequest.name());
        team.setCity(teamRequest.city());
        team.setGender(teamRequest.gender());
        team.setYearGroup(teamRequest.yearGroup());

        Team updatedTeam = teamRepository.save(team);
        log.info("Команда с ID {} обновлена", id);
        return convertToResponse(updatedTeam);
    }

    @Override
    public void deleteTeam(UUID id, UUID currentUserId) {
        log.info("Удаление команды с ID: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Команда не найдена: " + id));

        if (!isTeamOwner(id, currentUserId)) {
            throw new SecurityException("Недостаточно прав для удаления команды");
        }

        teamRepository.delete(team);
        log.info("Команда с ID {} удалена", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTeamOwner(UUID teamId, UUID userId) {
        return teamRepository.existsByTeamIdAndCoachId(teamId, userId);
    }

    private TeamResponse convertToResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getCity(),
                team.getGender(),
                team.getYearGroup(),
                team.getFullDisplayName(),
                team.getCoach().getId(),
                team.getCoach().getFullName(),
                team.getCreatedAt()
        );
    }
}