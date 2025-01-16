package com.example.TaskManagementSystem.Models;

import java.sql.Timestamp;

public class Task {

    private Long id;
    private String description;
    private Timestamp createdAt;
    private Timestamp dueDate;
    private Long assignedUserId;
    private String bossEmail; // Adăugăm email-ul pentru a căuta AssignedUserId

    // Getters și Setters
    public String getEmail() {
        return bossEmail;
    }

    public void setEmail(String email) {
        this.bossEmail = email;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
