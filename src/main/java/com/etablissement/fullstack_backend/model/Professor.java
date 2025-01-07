package com.etablissement.fullstack_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Professor")
public class Professor extends User {

    @Column
    private String department;

    @OneToMany(mappedBy = "professor")
    @JsonIgnoreProperties("professor")
    private Set<Schedule> schedules = new HashSet<>();

    // Default constructor
    public Professor() {
        super();
    }

    // Constructor with fields
    public Professor(String username, String password, String email, String firstName,
                   String lastName, String department) {
        super(username, password, email, firstName, lastName);
        this.department = department;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    // Helper methods
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setProfessor(this);
    }

    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setProfessor(null);
    }

    @Override
    public String toString() {
        return "Professor{" +
                super.toString() +
                ", department='" + department + '\'' +
                '}';
    }
}