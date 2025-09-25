package ru.school.tournamenthub.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.tournamenthub.dto.request.UserRequest;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Создание пользователя с username: {}", userRequest.getUsername());
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Пользователь с username '" + userRequest.getUsername() + "' уже существует");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Пользователь с email '" + userRequest.getEmail() + "' уже существует");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setFullName(userRequest.getFullName());
        user.setIsActive(true); // Новый пользователь активен по умолчанию

        // Сохраняем пользователя в базу данных
        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с ID: {}", savedUser.getId());

        // Преобразуем сущность в DTO для ответа
        return convertToResponse(savedUser);
    }

    /**
     * Получение пользователя по ID
     */
    @Transactional(readOnly = true) // Только для чтения - оптимизация
    public UserResponse getUserById(Long id) {
        log.info("Поиск пользователя с ID: {}", id);

        // Ищем пользователя в базе данных
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        return convertToResponse(user);
    }

    /**
     * Получение всех пользователей
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info("Получение списка всех пользователей");

        // Получаем всех пользователей из базы
        List<User> users = userRepository.findAll();

        // Преобразуем каждую сущность в DTO
        return users.stream()
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * Обновление пользователя
     */
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        log.info("Обновление пользователя с ID: {}", id);

        // Находим существующего пользователя
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        // Проверяем, не занят ли новый username другим пользователем
        if (!user.getUsername().equals(userRequest.getUsername()) &&
                userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username '" + userRequest.getUsername() + "' уже занят");
        }

        // Проверяем, не занят ли новый email другим пользователем
        if (!user.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email '" + userRequest.getEmail() + "' уже занят");
        }

        // Обновляем данные пользователя
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        user.setFullName(userRequest.getFullName());

        // Пароль обновляем только если он передан и не пустой
        if (userRequest.getPassword() != null && !userRequest.getPassword().trim().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Сохраняем обновленного пользователя
        User updatedUser = userRepository.save(user);
        log.info("Пользователь с ID {} обновлен", id);

        return convertToResponse(updatedUser);
    }


    public void deleteUser(Long id) {
        log.info("Деактивация пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        // Вместо физического удаления делаем деактивацию
        user.setIsActive(false);
        userRepository.save(user);

        log.info("Пользователь с ID {} деактивирован", id);
    }

    /**
     * Активация пользователя
     */
    public void activateUser(Long id) {
        log.info("Активация пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));

        user.setIsActive(true);
        userRepository.save(user);

        log.info("Пользователь с ID {} активирован", id);
    }

    /**
     * Поиск пользователя по username или email (для аутентификации)
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        log.debug("Поиск пользователя по username или email: {}", usernameOrEmail);
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    /**
     * Вспомогательный метод для преобразования User в UserResponse
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setFullName(user.getFullName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setIsActive(user.getIsActive());
        return response;
    }
}
