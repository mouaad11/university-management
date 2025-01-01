package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByDepartment(String department);

}
