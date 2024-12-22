package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.model.Schedule;
import com.etablissement.fullstack_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleRepository.findAll());
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return schedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleRepository.save(schedule));
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        return scheduleRepository.findById(id).map(schedule -> {
            schedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
            schedule.setStartTime(scheduleDetails.getStartTime());
            schedule.setEndTime(scheduleDetails.getEndTime());
            schedule.setSubject(scheduleDetails.getSubject());
            schedule.setType(scheduleDetails.getType());
            return ResponseEntity.ok(scheduleRepository.save(schedule));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable Long id) {
        return scheduleRepository.findById(id).map(schedule -> {
            scheduleRepository.delete(schedule);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
