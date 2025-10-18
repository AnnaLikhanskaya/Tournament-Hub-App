package ru.school.tournamenthub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.school.tournamenthub.dto.request.AuthRequest;
import ru.school.tournamenthub.dto.response.AuthResponse;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.security.JwtTokenProvider;
import ru.school.tournamenthub.service.AuthService;
import ru.school.tournamenthub.service.UserConverter;
import ru.school.tournamenthub.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserConverter userConverter;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        log.info("Попытка аутентификации пользователя: {}", authRequest.username());

        try {
            // Аутентификация jwt
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Получаем username из аутентификации
            String username = authentication.getName();

            // Получаем полные данные пользователя из базы через UserService
            User user = userService.findByUsername(username);

            // Генерируем JWT токен
            String jwt = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

            log.info("Пользователь {} успешно аутентифицирован", user.getUsername());

            // Преобразуем User в UserResponse
            UserResponse userResponse = userConverter.convertToResponse(user);

            return new AuthResponse(jwt, userResponse);
        } catch (Exception e) {
            log.error("Ошибка аутентификации для пользователя: {}", authRequest.username(), e);
            throw new RuntimeException("Ошибка аутентификации: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
        log.info("Пользователь вышел из системы");
    }

}