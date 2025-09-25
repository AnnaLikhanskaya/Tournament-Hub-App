package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.school.tournamenthub.model.enums.MatchStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Data

public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    /**
     * команда - хозяин
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    /**
     * команда - гости
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    /**
     * Раунд турнира (например: "Групповой этап", "1/4 финала", "Финал")
     */
    @Column(name = "round", length = 50)
    private String round;

    /**
     * Статус матча
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private MatchStatus matchStatus = MatchStatus.SCHEDULED;

    /**
     * Командная статистика матча (очки и фолы команд)
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_statistics_id") // добавить поле в таблицу matches
    private MatchStatistics matchStatistics;

    /**
     * Персональная статистика игроков в матче
     */
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchPlayerStatistics> playerStatistics = new ArrayList<>();

}