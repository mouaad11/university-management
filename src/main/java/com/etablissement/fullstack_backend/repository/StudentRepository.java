package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    List<Student> findByClasse_Id(Long classeId);
    boolean existsByStudentId(String studentId);
}
