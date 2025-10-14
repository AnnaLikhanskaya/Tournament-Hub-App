package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.school.tournamenthub.model.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "athletes") // исправил опечатку в названии таблицы
@Getter
@Setter
@ToString(exclude = {"team", "playerStatistics"})
@EqualsAndHashCode(exclude = {"team", "playerStatistics"})
@NoArgsConstructor

public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    /**
     * При удалении команды удаляются все ее спортсмены.
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    /**
     * Дата добавления спортсмена в систему
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Статистика выступлений спортсмена в матчах
     */
    @OneToMany(mappedBy = "athlete", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchPlayerStatistics> playerStatistics = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Получение возраста спортсмена на текущую дату
     */
    public Integer getAge() {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}