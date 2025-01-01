package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.exception.RoomNotAvailableException;
import com.etablissement.fullstack_backend.model.RoomRequest;
import com.etablissement.fullstack_backend.model.Schedule;
import com.etablissement.fullstack_backend.repository.RoomRequestRepository;
import com.etablissement.fullstack_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room-requests")
public class RoomRequestController {

    @Autowired
    private RoomRequestRepository roomRequestRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Create a room request (Professor and Student)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
    public ResponseEntity<RoomRequest> createRoomRequest(@RequestBody RoomRequest roomRequest) {
        // Check if the room is available at the requested time
        boolean isRoomAvailable = !scheduleRepository.existsByRoomAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                roomRequest.getRoom(), roomRequest.getDayOfWeek(), roomRequest.getStartTime(), roomRequest.getEndTime());

        if (!isRoomAvailable) {
            return ResponseEntity.badRequest().body(null); // Room is not available
        }

        roomRequest.setApproved(false); // Default to false
        return ResponseEntity.ok(roomRequestRepository.save(roomRequest));
    }

    // Get all pending requests (Admin)
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomRequest>> getPendingRequests() {
        return ResponseEntity.ok(roomRequestRepository.findByIsApprovedFalse());
    }

    // Approve a room request (Admin)
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomRequest> approveRoomRequest(@PathVariable Long id) {
        Optional<RoomRequest> roomRequestOptional = roomRequestRepository.findById(id);
        if (roomRequestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RoomRequest roomRequest = roomRequestOptional.get();
        if(!roomRequest.getRoom().getIsAvailable()){
            throw new RoomNotAvailableException("Cette salle est temporerement indisponible. Choisissez une autre salle svp.");        }
        roomRequest.setApproved(true);

        // Create a new schedule for the approved request
        Schedule schedule = new Schedule(
                null, // Classe (if needed)
                roomRequest.getProfessor(),
                roomRequest.getRoom(),
                roomRequest.getDayOfWeek(),
                roomRequest.getStartTime(),
                roomRequest.getEndTime(),
                roomRequest.getSubject(),
                roomRequest.getType()
        );
        scheduleRepository.save(schedule);

        return ResponseEntity.ok(roomRequestRepository.save(roomRequest));
    }

    // Reject a room request (Admin)
    @DeleteMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> rejectRoomRequest(@PathVariable Long id) {
        return roomRequestRepository.findById(id).map(roomRequest -> {
            roomRequestRepository.delete(roomRequest);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}