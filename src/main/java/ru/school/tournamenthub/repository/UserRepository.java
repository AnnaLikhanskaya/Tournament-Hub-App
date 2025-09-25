package ru.school.tournamenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByIsActiveFalse();

    List<User> findByFullNameContainingIgnoreCase(String name);

    // Кастомный запрос для поиска по имени пользователя или email
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    // Поиск пользователей, созданных после определенной даты
    List<User> findByCreatedAtAfter(LocalDateTime date);
}

