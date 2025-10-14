package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.school.tournamenthub.model.enums.Gender;
import ru.school.tournamenthub.model.enums.TournamentStatus;
import ru.school.tournamenthub.model.enums.TournamentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@ToString(exclude = {"createdBy", "season", "participations", "matches", "owner"})
@EqualsAndHashCode(exclude = {"createdBy", "season", "participations", "matches", "owner"})
@NoArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Описание турнира (необязательное)
     */
    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Место проведения турнира (город и название спорткомплекса)
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * Статус турнира
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TournamentStatus status = TournamentStatus.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_gender", length = 10)
    private Gender tournamentGender;

    /**
     * Возрастная категория турнира (например: "2011-2012")
     */
    @Column(name = "age_category", length = 20)
    private String ageCategory;

    /**
     * Дата создания турнира в системе
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Пользователь, создавший турнир
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;


    /**
     * Тип турнира
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_type", length = 20)
    private TournamentType tournamentType = TournamentType.REGULAR_SEASON;

    /**
     * Максимальное количество команд (для валидации)
     */
    @Column(name = "max_teams")
    private Integer maxTeams;

    /**
     * Формат турнира
     */
    @Column(name = "format", length = 50)
    private String format;

    /**
     * Все турниры должны принадлежать сезону (NOT NULL)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    /**
     * Список команд-участниц турнира
     */
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TournamentParticipation> participations = new ArrayList<>();

    /**
     * Список матчей, проведенных в рамках турнира
     */
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

//    @PrePersist
//    @PreUpdate
//    private void validateDates() {
//        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
//            throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала");
//        }
//    }

}