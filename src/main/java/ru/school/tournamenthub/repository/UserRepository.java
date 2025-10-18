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
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> { // Используем UUID вместо Long

    Optional<User> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Team t WHERE t.id = :teamId AND t.owner.id = :userId")
    boolean existsUserAsTeamOwner(@Param("userId") UUID userId, @Param("teamId") UUID teamId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Tournament t WHERE t.id = :tournamentId AND t.owner.id = :userId")
    boolean existsUserAsTournamentOwner(@Param("userId") UUID userId, @Param("tournamentId") UUID tournamentId);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByIsActiveFalse();

    List<User> findByFullNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    List<User> findByCreatedAtAfter(LocalDateTime date);
}