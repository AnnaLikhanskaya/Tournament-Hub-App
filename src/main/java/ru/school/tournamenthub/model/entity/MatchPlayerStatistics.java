package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Хранит индивидуальные показатели каждого игрока в конкретном матче.
 */
@Entity
@Table(name = "match_player_statistics")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class MatchPlayerStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Матч, в котором участвовал игрок
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    /**
     * Игрок, для которого записана статистика
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    /**
     * Количество очков, набранных игроком в матче
     */
    @Column(name = "points_scored")
    private Integer pointsScored = 0;

    /**
     * Количество фолов, совершенных игроком
     */
    @Column(name = "fouls_committed")
    private Integer foulsCommitted = 0;

    /**
     * Количество минут, проведенных игроком на площадке
     * Максимум 48 минут в баскетболе
     */
    @Column(name = "minutes_played")
    private Integer minutesPlayed = 0;
}