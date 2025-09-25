package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.school.tournamenthub.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность пользователя системы.
 * Представляет администраторов и тренеров, работающих с системой.
 */

@Entity
@Table(name = "users")
@Data

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    /**
     * Зашифрованный пароль пользователя
     * Лучше продумать шифрование
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * Роль пользователя в системе.
     * ADMIN - полный доступ ко всем функциям
     * COACH - доступ к управлению командами и турнирами
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    /**
     * Полное имя пользователя (ФИО)
     */
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /**
     * Дата и время создания учетной записи
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления учетной записи
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Флаг активности учетной записи
     * Неактивные пользователи не могут войти в систему
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Список команд, которые тренирует этот пользователь
     * Только пользователи с ролью COACH могут иметь команды
     */
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> coachedTeams = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}