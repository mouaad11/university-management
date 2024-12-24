package com.etablissement.fullstack_backend.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Student")
public class Student extends User {

    @Column
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    // Default constructor
    public Student() {
        super();
    }

    // Constructor with fields
    public Student(String username, String password, String email, String firstName,
                   String lastName, String studentId, Classe classe) {
        super(username, password, email, firstName, lastName);
        this.studentId = studentId;
        this.classe = classe;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Student{" +
                super.toString() +
                ", studentId='" + studentId + '\'' +
                ", classe=" + classe +
                '}';
    }
}