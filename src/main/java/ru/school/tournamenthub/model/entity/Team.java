package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.school.tournamenthub.model.enums.Gender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность команды.
 * Команда представляет собой группу спортсменов одного пола из одного города.
 * Уникально идентифицируется по комбинации: название + город + пол + год рождения.
 */
@Entity
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "city", "gender", "yearGroup", "coach_id"})
})
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    /**
     * Критически важно для разделения турниров и проверки соответствия игроков.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    /**
     * Год рождения игроков команды (например: "2011", "2010-2011")
     * Используется для возрастных категорий
     */

    @Column(name = "year_group", length = 10)
    private String yearGroup;

    /**
     * Полное отображаемое имя команды для отчетов и интерфейса.
     * Генерируется автоматически в формате: "Юноши, Зоркий 2011, Красногорск"
     */
    @Column(name = "full_display_name", length = 200)
    private String fullDisplayName;

    /**
     * Дата создания записи о команде
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Список спортсменов в команде
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Athlete> athletes = new ArrayList<>();

    /**
     * Список участий команды в турнирах
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TournamentParticipation> tournamentParticipations = new ArrayList<>();

    /**
     * Список домашних матчей команды
     */
    @OneToMany(mappedBy = "homeTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> homeMatches = new ArrayList<>();

    /**
     * Список гостевых матчей команды
     */
    @OneToMany(mappedBy = "awayTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> awayMatches = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Автоматическая генерация отображаемого имени
        if (fullDisplayName == null) {
            generateDisplayName();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        generateDisplayName();
    }

    /**
     * Генерация человеко-читаемого имени команды
     */
    private void generateDisplayName() {
        String genderText = gender == Gender.MALE ? "Юноши" : "Девушки";
        String yearText = yearGroup != null ? " " + yearGroup : "";
        this.fullDisplayName = String.format("%s, %s%s, %s", genderText, name, yearText, city);
    }
}