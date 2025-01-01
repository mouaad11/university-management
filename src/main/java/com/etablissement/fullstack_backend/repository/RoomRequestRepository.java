package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRequestRepository extends JpaRepository<RoomRequest, Long> {
    // Optional: Find all pending requests
    List<RoomRequest> findByIsApprovedFalse();
}