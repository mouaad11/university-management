package com.etablissement.fullstack_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "L3 Informatique"

    @Column(nullable = false)
    private String academicYear;

    @Column
    private String department;

    @OneToMany(mappedBy = "classe")
    @JsonIgnoreProperties({"students","classe"})
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    @JsonIgnoreProperties("classe")
    private Set<Schedule> schedules = new HashSet<>();

    // Default constructor
    public Classe() {}

    // Constructor with fields
    public Classe(String name, String academicYear, String department) {
        this.name = name;
        this.academicYear = academicYear;
        this.department = department;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    // Helper methods
    public void addStudent(Student student) {
        students.add(student);
        student.setClasse(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setClasse(null);
    }

    // equals, hashCode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classe aClasse = (Classe) o;
        return Objects.equals(id, aClasse.id) &&
                Objects.equals(name, aClasse.name) &&
                Objects.equals(academicYear, aClasse.academicYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, academicYear);
    }

    @Override
    public String toString() {
        return "Classe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}