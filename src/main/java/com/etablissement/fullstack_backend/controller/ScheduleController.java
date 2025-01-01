package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.model.Room;
import com.etablissement.fullstack_backend.model.Schedule;
import com.etablissement.fullstack_backend.repository.RoomRepository;
import com.etablissement.fullstack_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Get all schedules
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleRepository.findAll());
    }

    // Get schedule by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new schedule
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleRepository.save(schedule));
    }

    // Update an existing schedule
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        return scheduleRepository.findById(id).map(schedule -> {
            schedule.setClasse(scheduleDetails.getClasse());
            schedule.setProfessor(scheduleDetails.getProfessor());
            schedule.setRoom(scheduleDetails.getRoom());
            schedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
            schedule.setStartTime(scheduleDetails.getStartTime());
            schedule.setEndTime(scheduleDetails.getEndTime());
            schedule.setSubject(scheduleDetails.getSubject());
            schedule.setType(scheduleDetails.getType());
            return ResponseEntity.ok(scheduleRepository.save(schedule));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a schedule
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteSchedule(@PathVariable Long id) {
        return scheduleRepository.findById(id).map(schedule -> {
            scheduleRepository.delete(schedule);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all available rooms at the current system time
    @GetMapping("/available-rooms")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        LocalTime now = LocalTime.now();
        DayOfWeek today = DayOfWeek.from(java.time.LocalDate.now());

        // Fetch all rooms
        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            // Check if the room is occupied at the current time
            boolean isOccupied = scheduleRepository.existsByRoomAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                    room, today, now, now);

            if (!isOccupied) {
                availableRooms.add(room);
            }
        }

        return ResponseEntity.ok(availableRooms);
    }

    // Get weekly schedule for a specific room
    @GetMapping("/weekly-schedule/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<Map<DayOfWeek, Map<LocalTime, String>>> getWeeklySchedule(@PathVariable Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room room = roomOptional.get();
        Map<DayOfWeek, Map<LocalTime, String>> weeklySchedule = new HashMap<>();

        // Define the time slots (8:30 to 18:00, every 2 hours)
        List<LocalTime> timeSlots = Arrays.asList(
                LocalTime.of(8, 30),
                LocalTime.of(10, 30),
                LocalTime.of(12, 30),
                LocalTime.of(14, 30),
                LocalTime.of(16, 30)
        );

        // Iterate through each day of the week
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<LocalTime, String> dailySchedule = new HashMap<>();

            for (LocalTime slot : timeSlots) {
                // Check if the room is occupied at this time slot
                boolean isOccupied = scheduleRepository.existsByRoomAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        room, day, slot, slot.plusHours(2));

                dailySchedule.put(slot, isOccupied ? "Unavailable" : "Available");
            }

            weeklySchedule.put(day, dailySchedule);
        }

        return ResponseEntity.ok(weeklySchedule);
    }
}