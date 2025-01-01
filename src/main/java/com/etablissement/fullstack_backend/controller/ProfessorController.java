package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.model.Professor;
import com.etablissement.fullstack_backend.model.Schedule;
import com.etablissement.fullstack_backend.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    // Get all professors
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<List<Professor>> getAllProfessors() {
        return ResponseEntity.ok(professorRepository.findAll());
    }

    // Get professor by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return professor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new professor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        return ResponseEntity.ok(professorRepository.save(professor));
    }

    // Update an existing professor
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professorDetails) {
        return professorRepository.findById(id).map(professor -> {
            professor.setUsername(professorDetails.getUsername());
            professor.setPassword(professorDetails.getPassword());
            professor.setEmail(professorDetails.getEmail());
            professor.setFirstName(professorDetails.getFirstName());
            professor.setLastName(professorDetails.getLastName());
            professor.setDepartment(professorDetails.getDepartment());
            return ResponseEntity.ok(professorRepository.save(professor));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a professor
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteProfessor(@PathVariable Long id) {
        return professorRepository.findById(id).map(professor -> {
            professorRepository.delete(professor);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get professors by department
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    public ResponseEntity<List<Professor>> getProfessorsByDepartment(@PathVariable String department) {
        List<Professor> professors = professorRepository.findByDepartment(department);
        return ResponseEntity.ok(professors);
    }

    // Get all schedules for a specific professor
    @GetMapping("/{id}/schedules")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<Set<Schedule>> getProfessorSchedules(@PathVariable Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return professor.map(value -> ResponseEntity.ok(value.getSchedules()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}