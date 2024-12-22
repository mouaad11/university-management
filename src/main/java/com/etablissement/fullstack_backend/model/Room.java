package com.etablissement.fullstack_backend.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column
    private String building;

    @Column
    private String type; // Lecture hall, Lab, etc.

    @Column
    private Boolean isAvailable;

    // Default constructor
    public Room() {}

    // Constructor with fields
    public Room(String roomNumber, Integer capacity, String building, String type, Boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.building = building;
        this.type = type;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) &&
                Objects.equals(roomNumber, room.roomNumber) &&
                Objects.equals(building, room.building);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, building);
    }

    // toString method
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", capacity=" + capacity +
                ", building='" + building + '\'' +
                ", type='" + type + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}