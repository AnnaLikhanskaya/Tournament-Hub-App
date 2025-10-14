package ru.school.tournamenthub.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String field, String value) {
        super("Пользователь с " + field + " '" + value + "' уже существует");
    }
}