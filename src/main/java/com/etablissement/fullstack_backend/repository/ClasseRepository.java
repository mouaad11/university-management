package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    List<Classe> findByDepartment(String department);
    List<Classe> findByAcademicYear(String academicYear);
    boolean existsByNameAndAcademicYear(String name, String academicYear);
}
