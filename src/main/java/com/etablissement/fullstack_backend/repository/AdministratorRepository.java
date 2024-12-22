package com.etablissement.fullstack_backend.repository;

import com.etablissement.fullstack_backend.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    // Inherits basic CRUD operations from JpaRepository
    // Specific administrator queries can be added here
}
