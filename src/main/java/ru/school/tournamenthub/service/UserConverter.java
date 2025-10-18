package ru.school.tournamenthub.service;

import org.springframework.stereotype.Component;
import ru.school.tournamenthub.dto.response.UserResponse;
import ru.school.tournamenthub.model.entity.User;

@Component
public class UserConverter {

    public UserResponse convertToResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFullName(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getIsActive(),
                user.getVersion()
        );
    }
}