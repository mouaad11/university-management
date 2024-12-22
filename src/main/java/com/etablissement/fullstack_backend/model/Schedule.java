package com.etablissement.fullstack_backend.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String type; // COURSE, TD, TP

    // Default constructor
    public Schedule() {}

    // Constructor with fields
    public Schedule(Classe classe, Professor professor, Room room, DayOfWeek dayOfWeek,
                    LocalTime startTime, LocalTime endTime, String subject, String type) {
        this.classe = classe;
        this.professor = professor;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // equals, hashCode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) &&
                Objects.equals(classe, schedule.classe) &&
                Objects.equals(dayOfWeek, schedule.dayOfWeek) &&
                Objects.equals(startTime, schedule.startTime) &&
                Objects.equals(endTime, schedule.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, classe, dayOfWeek, startTime, endTime);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", classe=" + classe +
                ", professor=" + professor +
                ", room=" + room +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}