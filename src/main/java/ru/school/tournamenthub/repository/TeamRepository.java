package ru.school.tournamenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.school.tournamenthub.model.entity.Team;
import ru.school.tournamenthub.model.enums.Gender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findByCoachId(UUID coachId);

    List<Team> findByCityAndGender(String city, Gender gender);

    Optional<Team> findByNameAndCityAndGenderAndYearGroup(
            String name, String city, Gender gender, String yearGroup);

    boolean existsByNameAndCityAndGenderAndYearGroupAndCoachId(
            String name, String city, Gender gender, String yearGroup, UUID coachId);

    @Query("SELECT COUNT(t) > 0 FROM Team t WHERE t.id = :teamId AND t.coach.id = :coachId")
    boolean existsByTeamIdAndCoachId(@Param("teamId") UUID teamId, @Param("coachId") UUID coachId);

    boolean existsByNameAndCityAndGenderAndYearGroupAndCoachIdAndIdNot(
            String name, String city, Gender gender, String yearGroup, UUID coachId, UUID id);
}