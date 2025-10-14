package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность командной статистики матча.
 * Хранит общие показатели команд в матче (очки, фолы).
 */
@Entity
@Table(name = "match_team_statistics")
@Getter
@Setter
@ToString(exclude = {"match", "recordedBy"})
@EqualsAndHashCode(exclude = {"match", "recordedBy"})
@NoArgsConstructor
public class MatchStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Матч, к которому относится статистика
     * Связь один-к-одному (у каждого матча одна запись командной статистики)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    /**
     * Количество очков, набранных хозяевами
     */
    @Column(name = "home_team_score")
    private Integer homeTeamScore = 0;

    /**
     * Количество очков, набранных гостями
     */
    @Column(name = "away_team_score")
    private Integer awayTeamScore = 0;

    /**
     * Количество фолов хозяев
     */
    @Column(name = "home_team_fouls")
    private Integer homeTeamFouls = 0;

    /**
     * Количество фолов гостей
     */
    @Column(name = "away_team_fouls")
    private Integer awayTeamFouls = 0;

    /**
     * Пользователь, внесший статистику
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    /**
     * Дата и время внесения статистики
     */
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}