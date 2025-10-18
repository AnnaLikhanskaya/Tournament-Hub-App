package ru.school.tournamenthub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.tournamenthub.dto.request.UserRequest;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.exception.UserAlreadyExistsException;
import ru.school.tournamenthub.exception.UserNotFoundException;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.model.enums.UserRole;
import ru.school.tournamenthub.repository.UserRepository;
import ru.school.tournamenthub.service.UserConverter;
import ru.school.tournamenthub.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Создание пользователя с username: {}", userRequest.username());

        // Валидация входных данных
        validateUserRequest(userRequest);

        if (userRepository.existsByUsername(userRequest.username())) {
            log.warn("Попытка создания пользователя с существующим username: {}", userRequest.username());
            throw new UserAlreadyExistsException("username", userRequest.username());
        }
        if (userRepository.existsByEmail(userRequest.email())) {
            log.warn("Попытка создания пользователя с существующим email: {}", userRequest.email());
            throw new UserAlreadyExistsException("email", userRequest.email());
        }

        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .passwordHash(passwordEncoder.encode(userRequest.password()))
                .role(userRequest.role() != null ? userRequest.role() : UserRole.COACH) // По умолчанию COACH
                .fullName(userRequest.fullName())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с ID: {}", savedUser.getId());

        return userConverter.convertToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        log.info("Поиск пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userConverter.convertToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info("Получение списка всех пользователей");

        return userRepository.findAll().stream()
                .map(userConverter::convertToResponse)
                .toList(); // Java 16+ вместо .collect(Collectors.toList())
    }

    @Override
    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Проверяем уникальность username
        if (!user.getUsername().equals(userRequest.username()) &&
                userRepository.existsByUsername(userRequest.username())) {
            throw new UserAlreadyExistsException("username", userRequest.username());
        }

        // Проверяем уникальность email
        if (!user.getEmail().equals(userRequest.email()) &&
                userRepository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistsException("email", userRequest.email());
        }

        // Обновляем данные
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setRole(userRequest.role());
        user.setFullName(userRequest.fullName());

        // Пароль обновляем только если передан
        if (userRequest.password() != null && !userRequest.password().trim().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userRequest.password()));
        }

        User updatedUser = userRepository.save(user);
        log.info("Пользователь с ID {} обновлен", id);

        return userConverter.convertToResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Деактивация пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Проверяем, не деактивирован ли уже
        if (!user.getIsActive()) {
            throw new IllegalStateException("Пользователь уже деактивирован");
        }

        user.setIsActive(false);
        userRepository.save(user);
        log.info("Пользователь с ID {} деактивирован", id);
    }

    @Override
    public UserResponse activateUser(UUID id) {
        log.info("Активация пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Проверяем, не активирован ли уже
        if (user.getIsActive()) {
            throw new IllegalStateException("Пользователь уже активен");
        }

        user.setIsActive(true);
        User saveUser = userRepository.save(user);
        log.info("Пользователь с ID {} активирован", id);
        return userConverter.convertToResponse(saveUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // УБИРАЕМ debug - это частый запрос, засоряет логи
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (userRequest.username() == null || userRequest.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username не может быть пустым");
        }
        if (userRequest.email() == null || userRequest.email().trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!isValidEmail(userRequest.email())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
        if (userRequest.password() == null || userRequest.password().trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (userRequest.password().length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
}