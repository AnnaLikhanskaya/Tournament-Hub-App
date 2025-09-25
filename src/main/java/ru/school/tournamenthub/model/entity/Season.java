package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Сущность сезона.
 * Сезон представляет собой временной период (например, 2024-2025 спортивный год),
 * в рамках которого проводятся турниры.
 */

@Entity
@Table(name = "seasons")
@Data
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название сезона в формате "2024-2025"
     * уникально в системе!
     */

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "start_year", nullable = false)
    private Integer startYear;

    @Column(name = "end_year", nullable = false)
    private Integer endYear;

    /**
     * Только один сезон может быть активным в системе.
     */
    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "description")
    private String description;

    /**
     * Дата создания записи в системе
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Связь в турнире - один сезон ко многим турнирам
     */

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tournament> tournaments = new ArrayList<>();


    /**
     * Предустановка даты создания перед сохранением
     */

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}