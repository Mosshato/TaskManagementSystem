package com.example.TaskManagementSystem.Models;

public class Role {
    private Long id; // Auto-generated ID
    private String roleName;

    // Constructors
    public Role() {}

    public Role(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // Typically, the ID is set by the database, so no setter for ID

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
