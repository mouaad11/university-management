package com.etablissement.fullstack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Professor teacher;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column
    private String purpose;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    // Default constructor
    public Reservation() {}

    // Constructor with fields
    public Reservation(Room room, Professor teacher, LocalDateTime startTime, LocalDateTime endTime, String purpose, String status) {
        this.room = room;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Professor getProfessor() {
        return teacher;
    }

    public void setProfessor(Professor teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(room, that.room) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, room, startTime, endTime);
    }

    // toString method
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", room=" + room +
                ", teacher=" + teacher +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
