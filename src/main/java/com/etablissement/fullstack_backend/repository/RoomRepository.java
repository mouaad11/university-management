package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByBuilding(String building);
    List<Room> findByType(String type);
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT DISTINCT s.room.id FROM Schedule s WHERE " +
            "s.dayOfWeek = :dayOfWeek AND " +
            "((s.startTime <= :endTime AND s.endTime >= :startTime) OR " +
            "(s.startTime >= :startTime AND s.startTime < :endTime)))")
    List<Room> findAvailableRooms(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    @Query("SELECT r FROM Room r WHERE r.capacity >= :minCapacity AND r.id NOT IN " +
            "(SELECT DISTINCT s.room.id FROM Schedule s WHERE " +
            "s.dayOfWeek = :dayOfWeek AND " +
            "((s.startTime <= :endTime AND s.endTime >= :startTime) OR " +
            "(s.startTime >= :startTime AND s.startTime < :endTime)))")
    List<Room> findAvailableRoomsByCapacity(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Integer minCapacity
    );
}
