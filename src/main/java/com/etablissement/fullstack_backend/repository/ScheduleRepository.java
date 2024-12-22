package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByClasse_Id(Long classeId);  // Should reference class_.id
    List<Schedule> findByProfessor_Id(Long professorId);
    List<Schedule> findByRoom_Id(Long roomId);

    @Query("SELECT s FROM Schedule s WHERE s.room.id = :roomId " +
            "AND s.dayOfWeek = :dayOfWeek " +
            "AND ((s.startTime <= :endTime AND s.endTime >= :startTime) " +
            "OR (s.startTime >= :startTime AND s.startTime < :endTime))")
    List<Schedule> findConflictingSchedules(
            Long roomId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    List<Schedule> findByDayOfWeekAndRoom_Id(DayOfWeek dayOfWeek, Long roomId);
}
