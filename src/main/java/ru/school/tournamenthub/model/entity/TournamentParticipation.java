package ru.school.tournamenthub.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность участия команды в турнире.
 * Связывает команды с турнирами и хранит результаты участия.
 */

@Entity
@Table(name = "tournament_participations")
@Getter
@Setter
@ToString(exclude = {"tournament", "team"})
@EqualsAndHashCode(exclude = {"tournament", "team"})
@NoArgsConstructor
public class TournamentParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Турнир, в котором участвует команда
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    /**
     * Команда-участник
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    /**
     * Название группы, в которой играет команда (если турнир групповой)
     */
    @Column(name = "group_name", length = 20)
    private String groupName;

    /**
     * Финальная позиция команды в турнире
     */
    @Column(name = "final_position")
    private Integer finalPosition;

}