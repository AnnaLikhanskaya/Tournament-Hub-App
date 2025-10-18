package ru.school.tournamenthub.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Пользователь с ID " + id + " не найден");
    }

    public UserNotFoundException(String username) {
        super("Пользователь '" + username + "' не найден");
    }
}