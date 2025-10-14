package ru.school.tournamenthub.service;

import ru.school.tournamenthub.dto.request.AuthRequest;
import ru.school.tournamenthub.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Аутентификация пользователя
     */
    AuthResponse authenticate(AuthRequest authRequest);

    /**
     * Выход пользователя из системы
     */
    void logout();
}