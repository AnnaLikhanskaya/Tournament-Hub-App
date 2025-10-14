package ru.school.tournamenthub.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    /**
     * Получение текущего аутентифицированного пользователя
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Пользователь не аутентифицирован");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userService.findByUsername(userDetails.getUsername());
        } else if (principal instanceof String username) {
            return userService.findByUsername(username);
        } else {
            throw new SecurityException("Неизвестный тип principal: " + principal.getClass());
        }
    }

    /**
     * Получение ID текущего пользователя
     */
    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Проверка, является ли текущий пользователь владельцем команды
     */
    public boolean isTeamOwner(UUID teamId) { // МЕНЯЕМ Long на UUID
        try {
            UUID currentUserId = getCurrentUserId();
            // Здесь должна быть логика проверки владения командой
            // Пока возвращаем true для администраторов
            return getCurrentUser().isAdmin();
        } catch (Exception e) {
            log.warn("Ошибка при проверке владения командой: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Проверка, является ли текущий пользователь владельцем турнира
     */
    public boolean isTournamentOwner(UUID tournamentId) {
        try {
            UUID currentUserId = getCurrentUserId();
            // Здесь должна быть логика проверки владения турниром
            // Пока возвращаем true для администраторов
            return getCurrentUser().isAdmin();
        } catch (Exception e) {
            log.warn("Ошибка при проверке владения турниром: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Проверка, является ли текущий пользователь администратором
     */
    public boolean isAdmin() {
        try {
            return getCurrentUser().isAdmin();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверка, является ли текущий пользователь тренером
     */
    public boolean isCoach() {
        try {
            return getCurrentUser().isCoach();
        } catch (Exception e) {
            return false;
        }
    }
}