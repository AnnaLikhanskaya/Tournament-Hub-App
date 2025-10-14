package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.school.tournamenthub.model.enums.Gender;
import ru.school.tournamenthub.model.enums.ReportType;

import java.time.LocalDateTime;

/**
 * Сущность отчета.
 * Хранит сгенерированные отчеты для последующего доступа и истории.
 */
@Entity
@Table(name = "team_reports")
@Getter
@Setter
@ToString(exclude = {"season", "tournament", "team", "athlete", "generatedBy"})
@EqualsAndHashCode(exclude = {"season", "tournament", "team", "athlete", "generatedBy"})
@NoArgsConstructor
public class TeamReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип отчета (определяет структуру данных в reportData)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;

    // ЧЕЛОВЕКО-ЧИТАЕМЫЕ ИДЕНТИФИКАТОРЫ ДЛЯ ПРОСТОТЫ ФОРМИРОВАНИЯ ОТЧЕТОВ

    /**
     * Название сезона (например: "2024-2025")
     * Используется для фильтрации без знания ID
     */
    @Column(name = "season_name", length = 50)
    private String seasonName;

    /**
     * Название турнира
     */
    @Column(name = "tournament_name", length = 100)
    private String tournamentName;

    /**
     * Отображаемое имя команды (например: "Девушки, Зоркий 2011, Красногорск")
     */
    @Column(name = "team_display_name", length = 200)
    private String teamDisplayName;

    /**
     * Имя игрока (для персональных отчетов)
     */
    @Column(name = "athlete_name", length = 100)
    private String athleteName;

    /**
     * Город для фильтрации (например: "Красногорск")
     */
    @Column(name = "city_filter", length = 50)
    private String cityFilter;

    /**
     * Пол для фильтрации (MALE/FEMALE)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_filter", length = 10)
    private Gender genderFilter;

    /**
     * Год рождения для фильтрации (например: "2011")
     */
    @Column(name = "year_group_filter", length = 10)
    private String yearGroupFilter;

    // ССЫЛКИ НА СУЩНОСТИ ДЛЯ ТОЧНОСТИ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;

    /**
     * Данные отчета в формате JSON
     * Содержит структурированную информацию в зависимости от типа отчета
     */
    @Column(name = "report_data", columnDefinition = "JSONB")
    private String reportData;

    /**
     * Дата и время генерации отчета
     */
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    /**
     * Пользователь, сгенерировавший отчет
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}
