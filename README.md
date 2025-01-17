# University Classroom/Schedules Management System - Backend

This is the backend component of the University Room Management System, built using **Spring Boot**. It provides a RESTful API for managing users, classes, schedules, rooms, and reservations. The backend is connected to a **MySQL** database and uses **JPA/Hibernate** for ORM, as well as Spring Security.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Database Schema](#database-schema)
4. [API Endpoints](#api-endpoints)
5. [Setup Instructions](#setup-instructions)
6. [Running the Application](#running-the-application)
7. [Configuration](#configuration)
8. [Contributing](#contributing)

---

## Project Overview

The backend is designed to support the following key functionalities:
- **User Management**: Hierarchical user roles (Administrator, Student, Professor).
- **Class Management**: Manage classes, academic years, and departments.
- **Schedule Management**: Weekly timetables for classes, professors, and rooms.
- **Room Management**: Room availability and reservations.
- **CRUD Operations**: All entities support Create, Read, Update, and Delete operations.

---

## Technologies Used

- **Spring Boot**: Framework for building the REST API.
- **MySQL**: Relational database for data storage.
- **JPA/Hibernate**: Object-Relational Mapping (ORM) for database interactions.
- **REST API**: Stateless API architecture.
- **Maven**: Build automation and dependency management.
- **CORS Configuration**: Configured for React frontend running on port 3000.

---

## Database Schema

The database schema is automatically updated using Hibernate's `auto-ddl` feature. Key entities include:

1. **User**: Base entity for all users.
   - `id`, `username`, `password`, `email`, `firstName`, `lastName`
2. **Student**: Extends `User`.
   - `studentId`, `ManyToOne` relationship with `Classe`.
3. **Professor**: Extends `User`.
   - `department`, `OneToMany` relationship with `Schedule`.
4. **Classe**: Represents a class.
   - `id`, `name`, `academicYear`, `department`
   - `OneToMany` relationships with `Student` and `Schedule`.
5. **Schedule**: Represents a weekly timetable.
   - `id`, `dayOfWeek`, `startTime`, `endTime`, `subject`, `type`
   - `ManyToOne` relationships with `Classe`, `Professor`, and `Room`.
6. **Room**: Represents a physical room.
   - `id`, `roomNumber`, `capacity`, `building`, `type`, `isAvailable`

---

## API Endpoints

### User Management
- `GET /api/users`: Get all users.
- `GET /api/users/{id}`: Get a user by ID.
- `POST /api/users`: Create a new user.
- `PUT /api/users/{id}`: Update a user.
- `DELETE /api/users/{id}`: Delete a user.

### Class Management
- `GET /api/classes`: Get all classes.
- `GET /api/classes/{id}`: Get a class by ID.
- `POST /api/classes`: Create a new class.
- `PUT /api/classes/{id}`: Update a class.
- `DELETE /api/classes/{id}`: Delete a class.

### Schedule Management
- `GET /api/schedules`: Get all schedules.
- `GET /api/schedules/{id}`: Get a schedule by ID.
- `POST /api/schedules`: Create a new schedule.
- `PUT /api/schedules/{id}`: Update a schedule.
- `DELETE /api/schedules/{id}`: Delete a schedule.

### Room Management
- `GET /api/rooms`: Get all rooms.
- `GET /api/rooms/{id}`: Get a room by ID.
- `POST /api/rooms`: Create a new room.
- `PUT /api/rooms/{id}`: Update a room.
- `DELETE /api/rooms/{id}`: Delete a room.

---

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/university-room-management-backend.git
   cd university-room-management-backend
   
  2. **Configure MySQL:**

  Update the application.properties file with your MySQL credentials:
      
        spring.datasource.url=jdbc:mysql://localhost:3306/room_management
        spring.datasource.username=root
        spring.datasource.password=yourpassword
        spring.jpa.hibernate.ddl-auto=update
      
  3. **Build the Project:**
     ```bash
      mvn clean install
  ##Running the Application
   **Run the application using Maven:**
    ```bash
    
    mvn spring-boot:run
    
  The backend will start on port 8080.

## Configuration
CORS: Configured to allow requests from http://localhost:3000.

Logging: Logging is enabled for debugging purposes.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

