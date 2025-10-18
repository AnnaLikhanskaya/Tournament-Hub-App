package ru.school.tournamenthub.service;


import ru.school.tournamenthub.dto.request.UserRequest;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    /**
     * Создание нового пользователя
     */
    UserResponse createUser(UserRequest userRequest);

    /**
     * Получение пользователя по ID
     */
    UserResponse getUserById(UUID id);

    /**
     * Получение всех пользователей
     */
    List<UserResponse> getAllUsers();

    /**
     * Обновление пользователя
     */
    UserResponse updateUser(UUID id, UserRequest userRequest);

    /**
     * Деактивация пользователя
     */
    void deleteUser(UUID id);

    /**
     * Активация пользователя
     *
     * @return
     */
    UserResponse activateUser(UUID id);

    /**
     * Поиск пользователя по username или email (для аутентификации)
     */
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    /**
     * Поиск пользователя по username для аутентификации
     */
    User findByUsername(String username);

}
