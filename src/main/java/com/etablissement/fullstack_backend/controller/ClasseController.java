package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.model.Classe;
import com.etablissement.fullstack_backend.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClasseController {

    @Autowired
    private ClasseRepository classeRepository;

    @GetMapping("/classes")
    public ResponseEntity<List<Classe>> getAllClasses() {
        // Correctly call findAll() on the instance of classeRepository
        return ResponseEntity.ok(classeRepository.findAll());
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<Classe> getClassById(@PathVariable Long id) {
        Optional<Classe> classeEntity = classeRepository.findById(id);
        return classeEntity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    @PostMapping("/classes")
    public ResponseEntity<Classe> createClass(@RequestBody Classe classeEntity) {
        return ResponseEntity.ok(classeRepository.save(classeEntity));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<Classe> updateClass(@PathVariable Long id, @RequestBody Classe classeDetails) {
        return classeRepository.findById(id).map(classeEntity -> {
            classeEntity.setName(classeDetails.getName());
            classeEntity.setAcademicYear(classeDetails.getAcademicYear());
            classeEntity.setDepartment(classeDetails.getDepartment());
            return ResponseEntity.ok(classeRepository.save(classeEntity));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Object> deleteClass(@PathVariable Long id) {
        return classeRepository.findById(id).map(classEntity -> {
            classeRepository.delete(classEntity);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}